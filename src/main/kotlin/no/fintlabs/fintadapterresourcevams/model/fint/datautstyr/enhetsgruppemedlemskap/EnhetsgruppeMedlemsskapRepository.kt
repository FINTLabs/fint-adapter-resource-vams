package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppemedlemskap

import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.sync.SyncType
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppeMedlemsskap
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource
import org.springframework.stereotype.Repository

@Repository
abstract class EnhetsgruppeMedlemsskapRepository(
    private val vamsClient: VamsClient
): ResourceRepository<EnhetsgruppemedlemskapResource> {

    suspend fun fetchResources(): SyncData<EnhetsgruppemedlemskapResource> {
        val data = vamsClient.getRequestData<MaskinGruppeMedlemsskap>("datautstyr/maskingruppemedlemskap/")
        .unwrap { it.toFintModel() }
        println("fetching EnhetsGruppeMedlemsskap from Vams, ${data.size} resources returned.")
        return SyncData(data, SyncType.FULL)
    }
}