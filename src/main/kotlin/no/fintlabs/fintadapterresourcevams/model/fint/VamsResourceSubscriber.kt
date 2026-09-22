package no.fintlabs.fintadapterresourcevams.model.fint

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourceSubscriber
import no.fintlabs.adapter.models.AdapterCapability
import no.fintlabs.adapter.models.sync.SyncPage
import no.fintlabs.adapter.models.sync.SyncPageEntry
import no.fintlabs.adapter.validator.ValidatorService
import no.novari.fint.model.resource.FintLinks
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

abstract class VamsResourceSubscriber<T : FintLinks>(
    private val webClient: WebClient,
    adapterProperties: AdapterProperties,
    publisher: VamsResourcePublisher<T>,
    validatorService: ValidatorService,
    private val resourceKey: String,
    private val resourceDisplayName: String,
    private val getIdentificationValue: (T) -> String?,
) : ResourceSubscriber<T, VamsResourcePublisher<T>>(
        webClient,
        adapterProperties,
        publisher,
        validatorService,
    ) {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun getCapability(): AdapterCapability = adapterProperties.getCapabilityByResource(resourceKey)

    override fun createSyncPageEntry(resource: T): SyncPageEntry = SyncPageEntry.of(getIdentificationValue(resource), resource)

    override fun sendPages(page: SyncPage): Mono<*> {
        val endpoint = "/provider${getCapability().entityUri}"

        log.info(
            "Posting {} sync page {} of {} to {}. corrId={}, orgId={}, adapterId={}, uriRef={}",
            resourceDisplayName,
            page.metadata.page,
            page.metadata.totalPages,
            endpoint,
            page.metadata.corrId,
            page.metadata.orgId,
            page.metadata.adapterId,
            page.metadata.uriRef,
        )

        return webClient
            .method(page.syncType.httpMethod)
            .uri(endpoint)
            .body(Mono.just(page), SyncPage::class.java)
            .retrieve()
            .toBodilessEntity()
            .doOnNext { response ->
                log.info(
                    "{} sync page {} of {} returned {}. corrId={}",
                    resourceDisplayName,
                    page.metadata.page,
                    page.metadata.totalPages,
                    response.statusCode,
                    page.metadata.corrId,
                )
            }.onErrorResume(WebClientResponseException::class.java) { exception ->
                log.error(
                    "{} sync page {} of {} failed with {} for endpoint {}. corrId={}, orgId={}, adapterId={}, uriRef={}, responseBody={}",
                    resourceDisplayName,
                    page.metadata.page,
                    page.metadata.totalPages,
                    exception.statusCode,
                    endpoint,
                    page.metadata.corrId,
                    page.metadata.orgId,
                    page.metadata.adapterId,
                    page.metadata.uriRef,
                    exception.responseBodyAsString,
                    exception,
                )
                Mono.error(exception)
            }
    }

    override fun onError(throwable: Throwable) {
        if (throwable is WebClientResponseException) {
            log.error(
                "{} subscriber failed with {}. orgId={}, adapterId={}, uriRef={}, responseBody={}",
                resourceDisplayName,
                throwable.statusCode,
                adapterProperties.orgId,
                adapterProperties.id,
                getCapability().entityUri,
                throwable.responseBodyAsString,
                throwable,
            )
        } else {
            log.error("{} subscriber failed: {}", resourceDisplayName, throwable.message, throwable)
        }
    }
}
