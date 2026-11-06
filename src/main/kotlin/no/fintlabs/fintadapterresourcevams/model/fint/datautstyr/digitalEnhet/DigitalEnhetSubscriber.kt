package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.validator.ValidatorService
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourceSubscriber
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

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
    )
