package no.fintlabs.fintadapterresourcevams

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

class FintClient(
    private val webClient: WebClient
) {

}