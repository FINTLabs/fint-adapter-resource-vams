package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppemedlemskap

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.validator.ValidatorService
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourceSubscriber
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class EnhetsgruppeMedlemskapSubscriber(
    @Qualifier("webClient")
    webClient: WebClient,
    adapterProperties: AdapterProperties,
    publisher: EnhetsgruppeMedlemskapPublisher,
    validatorService: ValidatorService,
) : VamsResourceSubscriber<EnhetsgruppemedlemskapResource>(
        webClient,
        adapterProperties,
        publisher,
        validatorService,
        "enhetsgruppemedlemskap",
        "EnhetsgruppeMedlemskap",
        { resource -> resource.getSystemId()?.getIdentifikatorverdi() },
    )
