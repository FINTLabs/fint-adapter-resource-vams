package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.validator.ValidatorService
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourceSubscriber
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class EnhetstypeSubscriber(
    @Qualifier("webClient")
    webClient: WebClient,
    adapterProperties: AdapterProperties,
    publisher: EnhetstypePublisher,
    validatorService: ValidatorService,
) : VamsResourceSubscriber<EnhetstypeResource>(
        webClient,
        adapterProperties,
        publisher,
        validatorService,
        "enhetstype",
        "Enhetstype",
        { resource -> resource.getSystemId()?.getIdentifikatorverdi() },
    )
