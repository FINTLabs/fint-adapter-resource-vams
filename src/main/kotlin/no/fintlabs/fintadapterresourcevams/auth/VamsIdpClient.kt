package no.fintlabs.fintadapterresourcevams.auth

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import no.fintlabs.fintadapterresourcevams.config.VamsClientProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.Instant

@Component
class VamsIdpClient(
    private val vamsProperties: VamsClientProperties,
    private val idpProperties: IdpProperties,
    webClientBuilder: WebClient.Builder,
) {
    private val idpWebClient = webClientBuilder.build()
    private val tokenMutex = Mutex()

    @Volatile
    private var cachedToken: CachedToken? = null

    suspend fun getBearerToken(): String =
        cachedToken
            ?.takeIf { it.isValid() }
            ?.accessToken
            ?: tokenMutex.withLock {
                cachedToken
                    ?.takeIf { it.isValid() }
                    ?.accessToken
                    ?: fetchBearerToken()
                        .also { cachedToken = it }
                        .accessToken
            }

    private suspend fun fetchBearerToken(): CachedToken {
        val formData =
            BodyInserters
                .fromFormData("grant_type", vamsProperties.grantType)
                .also {
                    if (vamsProperties.scope.isNotBlank()) {
                        it.with("scope", vamsProperties.scope)
                    }
                }

        val response =
            idpWebClient
                .post()
                .uri(idpProperties.vamsIdp)
                .headers {
                    it.setBasicAuth(vamsProperties.clientId, vamsProperties.clientSecret)
                }.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .bodyToMono<TokenResponse>()
                .awaitSingle()

        return response.accessToken
            ?.let { CachedToken(it, response.expiresIn) }
            ?: throw IllegalStateException("No token returned from token endpoint")
    }

    private data class CachedToken(
        val accessToken: String,
        val expiresAt: Instant,
    ) {
        constructor(accessToken: String, expiresInSeconds: Long) : this(
            accessToken,
            Instant.now().plusSeconds(expiresInSeconds - expiryMarginSeconds(expiresInSeconds)),
        )

        fun isValid(): Boolean = Instant.now().isBefore(expiresAt)

        companion object {
            private const val TOKEN_EXPIRY_MARGIN_SECONDS = 60L

            private fun expiryMarginSeconds(expiresInSeconds: Long): Long = minOf(TOKEN_EXPIRY_MARGIN_SECONDS, expiresInSeconds / 2)
        }
    }

    data class TokenResponse(
        @param:JsonProperty("token_type") val tokenType: String?,
        @param:JsonProperty("expires_in") val expiresIn: Long,
        @param:JsonProperty("ext_expires_in") val extExpiresIn: Long?,
        @param:JsonProperty("access_token") val accessToken: String?,
    )
}
