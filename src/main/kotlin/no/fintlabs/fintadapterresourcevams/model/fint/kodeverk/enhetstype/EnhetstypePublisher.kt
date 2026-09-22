package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourcePublisher
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import org.springframework.stereotype.Service

@Service
class EnhetstypePublisher(
    repository: EnhetstypeRepository,
    props: AdapterProperties,
) : VamsResourcePublisher<EnhetstypeResource>(
        repository,
        props,
        "enhetstype",
        "Enhetstype",
    )
