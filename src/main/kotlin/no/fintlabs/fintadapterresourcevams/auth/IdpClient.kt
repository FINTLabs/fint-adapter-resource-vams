package no.fintlabs.fintadapterresourcevams.auth

import kotlinx.coroutines.reactor.awaitSingle
import no.fintlabs.fintadapterresourcevams.config.FintAdapterProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

class IdpClient(
    @Qualifier("fintIdpWebClient")
    private val webClient: WebClient,
    private val fintAdapterProperties: FintAdapterProperties
) {

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