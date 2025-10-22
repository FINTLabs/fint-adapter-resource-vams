package no.fintlabs.fintadapterresourcevams.auth

import no.fintlabs.fintadapterresourcevams.config.FintAdapterProperties
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient

@Component
class VamsIdpClient(
    private val fintAdapterProperties: FintAdapterProperties,
    private val vamsWebClient: WebClient
) {
    suspend fun getVamsBearerToken(): String =
        vamsWebClient

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