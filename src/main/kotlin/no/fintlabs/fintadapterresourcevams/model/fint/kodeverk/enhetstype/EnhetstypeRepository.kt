package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype

import no.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.sync.SyncType
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategori
import org.springframework.stereotype.Repository

@Repository
abstract class EnhetstypeRepository(
    private val vamsClient: VamsClient
): ResourceRepository<EnhetstypeResource> {

    suspend fun fetchResources(): SyncData<EnhetstypeResource> {
        val data = vamsClient.getRequestData(
            "kodeverk/maskinvarekategori/", MaskinvareKategori::class.java
        )
        return SyncData(data.unwrap { it.toFintModel() }, SyncType.FULL)
    }
    override fun getUpdatedResources() = emptyList<EnhetstypeResource>()
}