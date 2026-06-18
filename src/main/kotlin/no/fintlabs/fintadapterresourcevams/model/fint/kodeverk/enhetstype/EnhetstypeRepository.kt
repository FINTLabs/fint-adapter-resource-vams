package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype

import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.sync.SyncType
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategori
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import org.springframework.stereotype.Repository

@Repository
abstract class EnhetstypeRepository(
    private val vamsClient: VamsClient
): ResourceRepository<EnhetstypeResource> {

    suspend fun fetchResources(): SyncData<EnhetstypeResource> {
        val data = vamsClient.getRequestData<MaskinvareKategori>("kodeverk/maskinvarekategori/")
            .unwrap { it.toFintModel() }
        println("fetching Enhetstype from Vams, ${data.size} resources returned.")
        return SyncData(data, SyncType.FULL)
    }
}