package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.plattform

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.validator.ValidatorService
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourceSubscriber
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class PlattformSubscriber(
    @Qualifier("webClient")
    webClient: WebClient,
    adapterProperties: AdapterProperties,
    publisher: PlattformPublisher,
    validatorService: ValidatorService,
) : VamsResourceSubscriber<PlattformResource>(
        webClient,
        adapterProperties,
        publisher,
        validatorService,
        "plattform",
        "Plattform",
        { resource -> resource.getSystemId()?.getIdentifikatorverdi() },
    )
