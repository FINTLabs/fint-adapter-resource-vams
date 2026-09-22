package no.fintlabs.fintadapterresourcevams.config

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class WebClientConfig(
    private val vamsClientProperties: VamsClientProperties,
) {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()

    @Bean("vamsWebClient")
    fun vamsWebClient(webClientBuilder: WebClient.Builder) =
        webClientBuilder
            .baseUrl(vamsClientProperties.baseUrl)
            .clientConnector(ReactorClientHttpConnector(vamsHttpClient()))
            .build()

    private fun vamsHttpClient(): HttpClient =
        HttpClient
            .create()
            .keepAlive(false)
}
