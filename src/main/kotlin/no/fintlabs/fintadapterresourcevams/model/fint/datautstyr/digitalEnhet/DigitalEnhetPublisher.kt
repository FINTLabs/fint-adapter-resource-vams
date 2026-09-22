package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourcePublisher
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import org.springframework.stereotype.Service

@Service
class DigitalEnhetPublisher(
    repository: DigitalEnhetRepository,
    props: AdapterProperties,
) : VamsResourcePublisher<DigitalEnhetResource>(
        repository,
        props,
        "digitalenhet",
        "DigitalEnhet",
    )
