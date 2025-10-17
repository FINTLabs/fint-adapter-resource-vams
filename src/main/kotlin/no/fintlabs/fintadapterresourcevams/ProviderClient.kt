package no.fintlabs.fintadapterresourcevams

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactor.awaitSingle
import no.fintlabs.adapter.models.AdapterContract
import no.fintlabs.fintadapterresourcevams.auth.IdpClient
import no.fintlabs.fintadapterresourcevams.config.FintAdapterProperties
import no.fintlabs.fintadapterresourcevams.config.ProviderProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ProviderClient(
    @Qualifier("providerWebClient")
    private val webClient: WebClient,
    private val idpClient: IdpClient,
    private val providerProperties: ProviderProperties,
    private val fintAdapterProperties: FintAdapterProperties,
) {

    companion object {
        private const val REGISTER = "/register"
    }

// TODO -- One thing at a time
//    @PostConstruct
//    suspend fun init() {
//        val register = register()
//        println("Register " + register.statusCode)
//    }

    suspend fun register() =
        webClient.post()
            .uri(REGISTER)
            .body(providerProperties.createContract(), AdapterContract::class.java)
            .retrieve()
            .toBodilessEntity()
            .awaitSingle()

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
            .header(AUTHORIZATION, "bearer ${idpClient.getBearerToken()}")
            .header("")
            .retrieve()


}