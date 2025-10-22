package no.fintlabs.fintadapterresourcevams.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    private val providerProperties: ProviderProperties,
    private val webClientBuilder: WebClient.Builder,
    private val vamsClientProperties: VamsClientProperties
) {

    @Bean("providerWebClient")
    fun providerWebClient() =
        webClientBuilder
            .baseUrl(providerProperties.baseUrl)
            .build()

    @Bean("vamsWebClient")
    fun vamsWebClient() =
        webClientBuilder
            .baseUrl(vamsClientProperties.baseUrl)
            .build()
}