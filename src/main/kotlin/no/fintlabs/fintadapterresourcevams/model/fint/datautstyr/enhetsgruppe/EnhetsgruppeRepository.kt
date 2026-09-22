package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppe

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppering
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class EnhetsgruppeRepository(
    private val vamsClient: VamsClient,
) : ResourceRepository<EnhetsgruppeResource> {
    override fun getResources(): List<EnhetsgruppeResource> =
        runBlocking {
            val data =
                vamsClient
                    .getRequestData("datautstyr/maskingruppering/", MaskinGruppering::class.java)
                    .unwrap { it.toFintModel() }
                    .filterNotNull()
            log.info("Fetching Enhetsgruppe from Vams, {} resources returned.", data.size)
            data
        }

    override fun getUpdatedResources(): List<EnhetsgruppeResource> = emptyList()

    companion object {
        private val log = LoggerFactory.getLogger(EnhetsgruppeRepository::class.java)
    }
}
