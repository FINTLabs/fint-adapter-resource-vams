package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet

import no.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
abstract class DigitalEnhetPublisher(
    private val repository: DigitalEnhetRepository,
    props: AdapterProperties
) : ResourcePublisher<DigitalEnhetResource, DigitalEnhetRepository>(repository, props) {

    @Scheduled(cron = "\${fint.cron}")
    suspend fun performFullSync() {
        println("\n :: Starting Fullsync of DigitalEnhet :: \n")
        submit(repository.fetchResources())
    }
}