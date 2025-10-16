package no.fintlabs.fintadapterresourcevams.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    private val idpProperties: IdpProperties,
) {

    @Bean("fintIdpWebClient")
    fun fintIdpWebClient() =
        WebClient.builder()
            .baseUrl(idpProperties.fintIdp)
            .build()
}