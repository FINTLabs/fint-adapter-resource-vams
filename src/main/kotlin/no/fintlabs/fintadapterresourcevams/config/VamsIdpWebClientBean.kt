package no.fintlabs.fintadapterresourcevams.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class VamsIdpWebClientBean {

    @Bean("VamsIdpWebClient")
    fun idpWebClient(webClientBuilder: WebClient.Builder): WebClient = webClientBuilder.build()
}