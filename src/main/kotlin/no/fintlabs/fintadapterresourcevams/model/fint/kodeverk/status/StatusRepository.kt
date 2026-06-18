package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.status

import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.sync.SyncType
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.Status
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource
import org.springframework.stereotype.Repository

@Repository
abstract class StatusRepository(
    private val vamsClient: VamsClient
): ResourceRepository<StatusResource> {

    suspend fun fetchResources(): SyncData<StatusResource> {
        val data = vamsClient.getRequestData<Status>("datautstyr/status/")
            .unwrap { it.toFintModel() }
        println("fetching Status from Vams. ${data.size} resources returned.")
        return SyncData(data, SyncType.FULL)
    }
}