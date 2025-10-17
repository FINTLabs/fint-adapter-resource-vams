package no.fintlabs.fintadapterresourcevams.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    private val idpProperties: IdpProperties,
    private val providerProperties: ProviderProperties,
    private val webClientBuilder: WebClient.Builder
) {

    @Bean("fintIdpWebClient")
    fun fintIdpWebClient() =
        webClientBuilder
            .baseUrl(idpProperties.fintIdp)
            .build()

    @Bean("providerWebClient")
    fun providerWebClient() =
        webClientBuilder
            .baseUrl(providerProperties.baseUrl)
            .build()
}