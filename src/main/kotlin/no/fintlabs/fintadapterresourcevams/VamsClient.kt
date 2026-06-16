package no.fintlabs.fintadapterresourcevams

import kotlinx.coroutines.reactor.awaitSingle
import no.novari.fint.model.resource.FintLinks
import no.fintlabs.adapter.models.AdapterContract
import no.fintlabs.fintadapterresourcevams.auth.VamsIdpClient
import no.fintlabs.fintadapterresourcevams.config.FintAdapterProperties
import no.fintlabs.fintadapterresourcevams.config.ProviderProperties
import no.fintlabs.fintadapterresourcevams.config.VamsClientProperties
import no.fintlabs.fintadapterresourcevams.model.vams.ResourceCollection
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class VamsClient(
    @Qualifier("providerWebClient")
    private val webClient: WebClient,
    private val vamsIdpClient: VamsIdpClient,
    private val vamsClientProperties: VamsClientProperties,
    private val providerProperties: ProviderProperties,
    private val fintAdapterProperties: FintAdapterProperties,
) {

    private fun ProviderProperties.createContract() = AdapterContract().apply {
        adapterId = this.adapterId
        orgId = this.orgId
        username = fintAdapterProperties.username
        heartbeatIntervalInMinutes = this.heartbeatIntervalInMinutes
        capabilities = this.capabilities
    }

    suspend fun <T: FintLinks>getRequestData(url: String): ResourceCollection<T> {
        val typeReference = object : ParameterizedTypeReference<ResourceCollection<T>>() {}

        val token = vamsIdpClient.getBearerToken()

        return webClient.get()
            .uri(vamsClientProperties.baseUrl + url)
            .header(AUTHORIZATION, "Bearer $token")
            .header("CountyCode", vamsClientProperties.countyCode)
            .header("apiToken", vamsClientProperties.apiToken)
            .retrieve()
            .bodyToMono(typeReference)
            .awaitSingle()
    }
}