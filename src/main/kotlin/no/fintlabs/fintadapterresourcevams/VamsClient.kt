package no.fintlabs.fintadapterresourcevams

import kotlinx.coroutines.reactor.awaitSingle
import no.fintlabs.fintadapterresourcevams.auth.VamsIdpClient
import no.fintlabs.fintadapterresourcevams.config.VamsClientProperties
import no.fintlabs.fintadapterresourcevams.model.vams.ResourceCollection
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class VamsClient(
    @Qualifier("vamsWebClient")
    private val webClient: WebClient,
    private val vamsIdpClient: VamsIdpClient,
    private val vamsClientProperties: VamsClientProperties,
) {
    suspend fun <T>getRequestData(url: String): ResourceCollection<T> {
        val typeReference = object : ParameterizedTypeReference<ResourceCollection<T>>() {}

        val token = vamsIdpClient.getBearerToken()

        return webClient.get()
            .uri(vamsClientProperties.baseUrl + url)
            .headers {
                it.setBearerAuth(token)
                it["CountyCode"] = vamsClientProperties.countyCode
                it["apiToken"] = vamsClientProperties.apiToken
            }
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono<String>()
                    .map { body ->
                        IllegalStateException(
                            "VAMS failed with ${response.statusCode()}: $body"
                        )
                    }
            }
            .bodyToMono(typeReference)
            .awaitSingle()
    }
}