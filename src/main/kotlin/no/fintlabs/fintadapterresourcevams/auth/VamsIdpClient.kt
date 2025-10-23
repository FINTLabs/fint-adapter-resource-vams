package no.fintlabs.fintadapterresourcevams.auth

import kotlinx.coroutines.reactor.awaitSingle
import no.fintlabs.fintadapterresourcevams.config.IdpProperties
import no.fintlabs.fintadapterresourcevams.config.VamsClientProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class VamsIdpClient(
    private val vamsProperties: VamsClientProperties,
    private val idpProperties: IdpProperties,
    @Qualifier("vamsWebClient")
    private val webClient: WebClient
) {

    suspend fun getBearerToken(): Any? {

        val token = webClient.get()
            .uri(idpProperties.vamsIdp)
            .header("grant_type", "client_credentials")
            .header("client_id", vamsProperties.clientId)
            .header("client_secret", vamsProperties.clientSecret)
            .header("scope", vamsProperties.scope)
            .header("county_code", vamsProperties.countyCode)
            .header("apiToken", vamsProperties.apiToken)
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()

        println("VamsIdpClient.getBearerToken: $token")
        return token
    }


//    private fun createFormData(): MultiValueMap<String, String> =
//        LinkedMultiValueMap<String, String>().apply {
//            add("grant_type", "client_credentials")
//            add("client_id", vamsProperties.clientId)
//            add("client_secret", vamsProperties.clientSecret)
//            add("scope", "fint-client")
//        }


}