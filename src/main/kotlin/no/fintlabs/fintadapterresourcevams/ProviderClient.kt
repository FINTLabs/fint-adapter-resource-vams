package no.fintlabs.fintadapterresourcevams

import jakarta.annotation.PostConstruct
import no.fintlabs.fintadapterresourcevams.config.FintAdapterProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ProviderClient(private val fintAdapterProperties: FintAdapterProperties) {
    private val webClient: WebClient = WebClient.create("http://localhost:8080")


    @PostConstruct
    fun init() {
        webClient
        println(fintAdapterProperties)
    }

    // Header<Authorization, Bearer token>
    // Du får bearer token fra IDP-en vår
    fun getRequestData() =
        webClient.get()
            .header("")
            .header("")
            .retrieve()



}