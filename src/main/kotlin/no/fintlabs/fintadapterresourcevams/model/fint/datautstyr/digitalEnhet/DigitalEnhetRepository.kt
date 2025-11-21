package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet

import no.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.sync.SyncType
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.Maskinvare
import org.springframework.stereotype.Repository

@Repository
abstract class DigitalEnhetRepository(
    private val vamsClient: VamsClient
): ResourceRepository<DigitalEnhetResource> {

    suspend fun fetchResources(): SyncData<DigitalEnhetResource> {
        val data = vamsClient.getRequestData(
            "datautstyr/maskinvare/", Maskinvare::class.java
        )
        return SyncData(data.unwrap { it.toFintModel() }, SyncType.FULL)
    }
    override fun getUpdatedResources() = emptyList<DigitalEnhetResource>()
}