package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppe

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourcePublisher
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import org.springframework.stereotype.Service

@Service
class EnhetsgruppePublisher(
    repository: EnhetsgruppeRepository,
    props: AdapterProperties,
) : VamsResourcePublisher<EnhetsgruppeResource>(
        repository,
        props,
        "enhetsgruppe",
        "Enhetsgruppe",
    )
