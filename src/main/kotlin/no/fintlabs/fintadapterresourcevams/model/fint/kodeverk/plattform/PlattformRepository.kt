package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.plattform

import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.sync.SyncType
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.Plattform
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import org.springframework.stereotype.Repository

@Repository
abstract class PlattformRepository(
    private val vamsClient: VamsClient
): ResourceRepository<PlattformResource> {

    suspend fun fetchResources(): SyncData<PlattformResource> {
        val data = vamsClient.getRequestData<Plattform>("datautstyr/plattform/")
            .unwrap { it.toFintModel() }
        println("fetching Plattform data from Vams, ${data.size} resources returned")
        return SyncData(data, SyncType.FULL)
    }
}