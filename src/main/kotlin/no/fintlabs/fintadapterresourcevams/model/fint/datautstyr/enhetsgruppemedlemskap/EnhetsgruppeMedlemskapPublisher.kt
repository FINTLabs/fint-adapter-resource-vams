package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppemedlemskap

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourcePublisher
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource
import org.springframework.stereotype.Service

@Service
class EnhetsgruppeMedlemskapPublisher(
    repository: EnhetsgruppeMedlemsskapRepository,
    props: AdapterProperties,
) : VamsResourcePublisher<EnhetsgruppemedlemskapResource>(
        repository,
        props,
        "enhetsgruppemedlemskap",
        "EnhetsgruppeMedlemskap",
    )
