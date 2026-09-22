package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppemedlemskap

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppeMedlemsskap
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class EnhetsgruppeMedlemsskapRepository(
    private val vamsClient: VamsClient,
) : ResourceRepository<EnhetsgruppemedlemskapResource> {
    override fun getResources(): List<EnhetsgruppemedlemskapResource> =
        runBlocking {
            val data =
                vamsClient
                    .getRequestData("datautstyr/maskingruppemedlemskap/", MaskinGruppeMedlemsskap::class.java)
                    .unwrap { it.toFintModel() }
                    .filterNotNull()
            log.info("Fetching EnhetsgruppeMedlemsskap from Vams, {} resources returned.", data.size)
            data
        }

    override fun getUpdatedResources(): List<EnhetsgruppemedlemskapResource> = emptyList()

    companion object {
        private val log = LoggerFactory.getLogger(EnhetsgruppeMedlemsskapRepository::class.java)
    }
}
