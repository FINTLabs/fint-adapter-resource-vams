package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.models.sync.SyncPage
import no.fintlabs.adapter.validator.ValidatorService
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourceSubscriber
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import tools.jackson.databind.json.JsonMapper

@Service
class DigitalEnhetSubscriber(
    @Qualifier("webClient")
    webClient: WebClient,
    adapterProperties: AdapterProperties,
    publisher: DigitalEnhetPublisher,
    validatorService: ValidatorService,
) : VamsResourceSubscriber<DigitalEnhetResource>(
        webClient,
        adapterProperties,
        publisher,
        validatorService,
        "digitalenhet",
        "DigitalEnhet",
        { resource -> resource.getSystemId()?.getIdentifikatorverdi() },
    ) {
    private val objectMapper = JsonMapper.builder().build()

    override fun sendPages(page: SyncPage): Mono<*> {
        page.resources.forEach { entry ->
            val resource = entry.resource as? DigitalEnhetResource
            log.info(
                "FINT DigitalEnhet outgoing systemId={} corrId={} enhetstype={} json={}",
                entry.identifier,
                page.metadata.corrId,
                resource?.enhetstype?.map { it.href },
                objectMapper.writeValueAsString(entry.resource),
            )
        }
        return super.sendPages(page)
    }

    companion object {
        private val log = LoggerFactory.getLogger(DigitalEnhetSubscriber::class.java)
    }
}
