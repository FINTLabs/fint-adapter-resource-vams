package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppemedlemskap

import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
abstract class EnhetsgruppeMedlemsskapPublisher(
    private val repository: EnhetsgruppeMedlemsskapRepository,
    props: AdapterProperties
) : ResourcePublisher<EnhetsgruppemedlemskapResource, EnhetsgruppeMedlemsskapRepository>(repository, props)  {

    @Scheduled(cron = "\${fint.cron}")
    suspend fun performFullSync() {
        println("\n :: Starting Fullsync of EnhetsgruppeMedlemsskap :: \n")
        submit(repository.fetchResources())
    }
}