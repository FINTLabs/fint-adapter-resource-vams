package no.fintlabs.fintadapterresourcevams

import no.fintlabs.adapter.models.AdapterContract
import no.fintlabs.fintadapterresourcevams.auth.VamsIdpClient
import no.fintlabs.fintadapterresourcevams.config.FintAdapterProperties
import no.fintlabs.fintadapterresourcevams.config.ProviderProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class VamsClient(
    @Qualifier("providerWebClient")
    private val webClient: WebClient,

    private val vamsIdpClient: VamsIdpClient,
    private val providerProperties: ProviderProperties,
    private val fintAdapterProperties: FintAdapterProperties,
) {

// TODO -- One thing at a time
//    @PostConstruct
//    suspend fun init() {
//        val register = register()
//        println("Register " + register.statusCode)
//    }

    private fun ProviderProperties.createContract() = AdapterContract().apply {
        adapterId = this.adapterId
        orgId = this.orgId
        username = fintAdapterProperties.username
        heartbeatIntervalInMinutes = this.heartbeatIntervalInMinutes
        capabilities = this.capabilities
    }

    // Header<Authorization, Bearer token>
    suspend fun getRequestData() =
        webClient.get()
            .header(AUTHORIZATION, "bearer ${vamsIdpClient.getFintBearerToken()}")
            .header("")
            .retrieve()


}