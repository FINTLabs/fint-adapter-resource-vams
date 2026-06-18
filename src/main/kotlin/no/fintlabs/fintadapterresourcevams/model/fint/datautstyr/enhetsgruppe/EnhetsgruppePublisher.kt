package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppe


import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
abstract class EnhetsgruppePublisher(
    private val repository: EnhetsgruppeRepository,
    props: AdapterProperties
) : ResourcePublisher<EnhetsgruppeResource, EnhetsgruppeRepository>(repository, props) {

    @Scheduled(cron = "\${fint.cron}")
    fun performFullSync() = runBlocking {
        println("\n :: Starting Fullsync of Enhetsgruppe :: \n")
        submit(repository.fetchResources())
    }
}
