package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

//TODO: This is not supposed to be here..?
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