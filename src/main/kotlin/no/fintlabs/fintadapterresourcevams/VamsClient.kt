package no.fintlabs.fintadapterresourcevams

import kotlinx.coroutines.reactor.awaitSingle
import no.fintlabs.fintadapterresourcevams.auth.VamsIdpClient
import no.fintlabs.fintadapterresourcevams.config.VamsClientProperties
import no.fintlabs.fintadapterresourcevams.model.vams.ResourceCollection
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.ResolvableType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.util.retry.Retry
import java.time.Duration

@Component
class VamsClient(
    @param:Qualifier("vamsWebClient")
    private val webClient: WebClient,
    private val vamsIdpClient: VamsIdpClient,
    private val vamsClientProperties: VamsClientProperties,
) {
    suspend fun <T : Any> getRequestData(
        url: String,
        responseType: Class<T>,
    ): ResourceCollection<T> {
        val typeReference: ParameterizedTypeReference<ResourceCollection<T>> =
            ParameterizedTypeReference.forType(
                ResolvableType.forClassWithGenerics(ResourceCollection::class.java, responseType).type,
            )

        val token = vamsIdpClient.getBearerToken()

        return webClient
            .get()
            .uri(url)
            .headers {
                it.setBearerAuth(token)
                it["CountyCode"] = vamsClientProperties.countyCode
                it["apiToken"] = vamsClientProperties.apiToken
            }.retrieve()
            .onStatus({ it.isError }) { response ->
                response
                    .bodyToMono<String>()
                    .map { body ->
                        IllegalStateException(
                            "VAMS failed with ${response.statusCode()}: $body",
                        )
                    }
            }.bodyToMono(typeReference)
            .retryWhen(retryTransientRequestFailures(url))
            .awaitSingle()
    }

    private fun retryTransientRequestFailures(requestUrl: String): Retry =
        Retry
            .backoff(3, Duration.ofSeconds(2))
            .maxBackoff(Duration.ofSeconds(15))
            .filter { it is WebClientRequestException }
            .doBeforeRetry { retrySignal ->
                log.warn(
                    "Retrying VAMS request {} after transient transport error. Attempt {} of 3 failed: {}",
                    requestUrl,
                    retrySignal.totalRetries() + 1,
                    retrySignal.failure().message,
                )
            }.onRetryExhaustedThrow { _, retrySignal -> retrySignal.failure() }

    companion object {
        private val log = LoggerFactory.getLogger(VamsClient::class.java)
    }
}
