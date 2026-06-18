package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
abstract class EnhetstypePublisher(
    private val repository: EnhetstypeRepository,
    props: AdapterProperties
) : ResourcePublisher<EnhetstypeResource, EnhetstypeRepository>(repository, props) {

    @Scheduled(cron = "\${fint.cron}")
    fun performFullSync() = runBlocking {
        println("\n :: Starting Fullsync of Enhetstype :: \n")
        submit(repository.fetchResources())
    }
}