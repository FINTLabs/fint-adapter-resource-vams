package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype

import no.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
abstract class EnhetstypePublisher(
    private val repository: EnhetstypeRepository,
    props: AdapterProperties
) : ResourcePublisher<EnhetstypeResource, EnhetstypeRepository>(repository, props) {

    @Scheduled(cron = "\${fint.cron}")
    suspend fun performFullSync() {
        println("\n :: Starting Fullsync of Enhetsgruppe :: \n")
        submit(repository.fetchResources())
    }
}