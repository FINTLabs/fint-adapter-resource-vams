package no.fintlabs.fintadapterresourcevams.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientBuilderConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()
}