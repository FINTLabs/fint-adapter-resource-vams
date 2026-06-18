package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.plattform

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
abstract class PlattformPublisher(
    private val repository: PlattformRepository,
    props: AdapterProperties
): ResourcePublisher<PlattformResource, PlattformRepository>(repository, props) {

    @Scheduled(cron = "\${fint.cron}")
    fun performFullSync() = runBlocking {
        println("\n :: Starting Fullsync of Plattform :: \n")
        submit(repository.fetchResources())
    }
}