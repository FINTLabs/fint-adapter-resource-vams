package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.sync.SyncType
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
abstract class DigitalEnhetPublisher(
    private val repository: DigitalEnhetRepository,
    props: AdapterProperties
) : ResourcePublisher<DigitalEnhetResource, DigitalEnhetRepository>(repository, props) {

    // TODO; runBLocking in the other Publishers instead of suspend.
    //TODO: remove postConstruct and fixedDelay. They are only for testing.
    @PostConstruct
    @Scheduled(cron = "\${fint.cron}", fixedDelay = 600)
    fun performFullSync() = runBlocking {
        println("\n :: Starting Fullsync of DigitalEnhet :: \n")
        submit(
//            repository.fetchResources()
//            TODO: remove this after Auth testing complete
            SyncData<DigitalEnhetResource>(emptyList<DigitalEnhetResource>(), SyncType.FULL)
        )
    }
}