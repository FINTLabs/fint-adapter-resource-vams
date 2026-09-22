package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.plattform

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourcePublisher
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import org.springframework.stereotype.Service

@Service
class PlattformPublisher(
    repository: PlattformRepository,
    props: AdapterProperties,
) : VamsResourcePublisher<PlattformResource>(
        repository,
        props,
        "plattform",
        "Plattform",
    )
