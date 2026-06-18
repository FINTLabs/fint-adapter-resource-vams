package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.status

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
abstract class StatusPublisher(
    private val repository: StatusRepository,
    props: AdapterProperties
): ResourcePublisher<StatusResource, StatusRepository>(repository, props) {

    @Scheduled(cron = "\${fint.cron}")
    fun performFullSync() = runBlocking {
        println("\n :: Starting Fullsync of DigitalEnhet :: \n")
        submit(repository.fetchResources())
    }
}