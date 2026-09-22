package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.status

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.validator.ValidatorService
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourceSubscriber
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class StatusSubscriber(
    @Qualifier("webClient")
    webClient: WebClient,
    adapterProperties: AdapterProperties,
    publisher: StatusPublisher,
    validatorService: ValidatorService,
) : VamsResourceSubscriber<StatusResource>(
        webClient,
        adapterProperties,
        publisher,
        validatorService,
        "status",
        "Status",
        { resource -> resource.getSystemId()?.getIdentifikatorverdi() },
    )
