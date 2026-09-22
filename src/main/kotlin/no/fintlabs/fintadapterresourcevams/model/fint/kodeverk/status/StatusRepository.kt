package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.status

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.Status
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class StatusRepository(
    private val vamsClient: VamsClient,
) : ResourceRepository<StatusResource> {
    override fun getResources(): List<StatusResource> =
        runBlocking {
            val data =
                vamsClient
                    .getRequestData("kodeverk/status/", Status::class.java)
                    .unwrap { it.toFintModel() }
                    .filterNotNull()
            log.info("Fetching Status from Vams, {} resources returned.", data.size)
            data
        }

    override fun getUpdatedResources(): List<StatusResource> = emptyList()

    companion object {
        private val log = LoggerFactory.getLogger(StatusRepository::class.java)
    }
}
