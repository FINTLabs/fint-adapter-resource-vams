package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.status

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.fintadapterresourcevams.model.fint.VamsResourcePublisher
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource
import org.springframework.stereotype.Service

@Service
class StatusPublisher(
    repository: StatusRepository,
    props: AdapterProperties,
) : VamsResourcePublisher<StatusResource>(
        repository,
        props,
        "status",
        "Status",
    )
