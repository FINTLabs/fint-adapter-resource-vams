package no.fintlabs.fintadapterresourcevams.auth

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.coroutines.reactor.awaitSingle
import no.fintlabs.fintadapterresourcevams.config.IdpProperties
import no.fintlabs.fintadapterresourcevams.config.VamsClientProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class VamsIdpClient(
    private val vamsProperties: VamsClientProperties,
    private val idpProperties: IdpProperties,
    @Qualifier("VamsIdpWebClient")
    private val idpWebClient: WebClient
) {

    suspend fun getBearerToken(): String {
        val formData = BodyInserters
            .fromFormData("grant_type", "client_credentials")
            .also {
                if (vamsProperties.scope.isNotBlank()) {
                    it.with("scope", vamsProperties.scope)
                }
            }

        val response = idpWebClient.post()
            .uri(idpProperties.vamsIdp)
            .headers {
                it.setBasicAuth(vamsProperties.clientId, vamsProperties.clientSecret)
            }
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData)
            .retrieve()
            .bodyToMono<TokenResponse>()
            .awaitSingle()

        return response.accessToken
            ?: throw IllegalStateException("No token returned from token endpoint")
    }

    data class TokenResponse(
        @JsonProperty("token_type") val tokenType: String?,
        @JsonProperty("expires_in") val expiresIn: Long,
        @JsonProperty("ext_expires_in") val extExpiresIn: Long,
        @JsonProperty("access_token") val accessToken: String?,
    )


//    private fun createFormData(): MultiValueMap<String, String> =
//        LinkedMultiValueMap<String, String>().apply {
//            add("grant_type", "client_credentials")
//            add("client_id", vamsProperties.clientId)
//            add("client_secret", vamsProperties.clientSecret)
//            add("scope", "fint-client")
//        }


}