package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppe

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.validator.ValidatorService
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourceSubscriber
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class EnhetsgruppeSubscriber(
    @Qualifier("webClient")
    webClient: WebClient,
    adapterProperties: AdapterProperties,
    publisher: EnhetsgruppePublisher,
    validatorService: ValidatorService,
) : VamsResourceSubscriber<EnhetsgruppeResource>(
        webClient,
        adapterProperties,
        publisher,
        validatorService,
        "enhetsgruppe",
        "Enhetsgruppe",
        { resource -> resource.getSystemId()?.getIdentifikatorverdi() },
    )
