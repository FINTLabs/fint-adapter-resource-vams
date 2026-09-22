package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.plattform

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.Plattform
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class PlattformRepository(
    private val vamsClient: VamsClient,
) : ResourceRepository<PlattformResource> {
    override fun getResources(): List<PlattformResource> =
        runBlocking {
            val data =
                vamsClient
                    .getRequestData("kodeverk/plattform/", Plattform::class.java)
                    .unwrap { it.toFintModel() }
                    .filterNotNull()
            log.info("Fetching Plattform from Vams, {} resources returned.", data.size)
            data
        }

    override fun getUpdatedResources(): List<PlattformResource> = emptyList()

    companion object {
        private val log = LoggerFactory.getLogger(PlattformRepository::class.java)
    }
}
