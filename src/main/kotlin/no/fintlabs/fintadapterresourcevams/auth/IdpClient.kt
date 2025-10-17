package no.fintlabs.fintadapterresourcevams.auth

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactor.awaitSingle
import no.fintlabs.fintadapterresourcevams.config.FintAdapterProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
class IdpClient(
    @Qualifier("fintIdpWebClient")
    private val webClient: WebClient,
    private val fintAdapterProperties: FintAdapterProperties
) {
    @PostConstruct
    suspend fun init() {
        val token = getBearerToken()
        println("Token: $token")
    }

    suspend fun getBearerToken(): FintIdpResponse =
        webClient.post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(createFormData()))
            .retrieve()
            .bodyToMono(FintIdpResponse::class.java)
            .awaitSingle()

    private fun createFormData(): MultiValueMap<String, String> =
        LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "password")
            add("client_id", fintAdapterProperties.clientId)
            add("client_secret", fintAdapterProperties.clientSecret)
            add("username", fintAdapterProperties.username)
            add("password", fintAdapterProperties.password)
            add("scope", "fint-client")
        }
}