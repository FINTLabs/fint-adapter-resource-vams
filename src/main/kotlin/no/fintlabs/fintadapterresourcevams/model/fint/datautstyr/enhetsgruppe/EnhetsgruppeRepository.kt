package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppe

import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.sync.SyncType
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppering
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import org.springframework.stereotype.Repository

@Repository
abstract class EnhetsgruppeRepository(
    private val vamsClient: VamsClient
): ResourceRepository<EnhetsgruppeResource> {

    suspend fun fetchResources(): SyncData<EnhetsgruppeResource> {
        val data = vamsClient.getRequestData(
            "datautstyr/maskingruppering/", MaskinGruppering::class.java
        )
        return SyncData(data.unwrap { it.toFintModel() }, SyncType.FULL)
    }
    override fun getUpdatedResources() = emptyList<EnhetsgruppeResource>()
}