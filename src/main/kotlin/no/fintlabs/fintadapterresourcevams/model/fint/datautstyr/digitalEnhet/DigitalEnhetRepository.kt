package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.Maskinvare
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class DigitalEnhetRepository(
    private val vamsClient: VamsClient,
) : ResourceRepository<DigitalEnhetResource> {
    override fun getResources(): List<DigitalEnhetResource> =
        runBlocking {
            val data =
                vamsClient
                    .getRequestData("datautstyr/maskinvare/", Maskinvare::class.java)
                    .unwrap { it.toFintModel() }
                    .filterNotNull()
            log.info("Fetching DigitalEnhet from Vams, {} resources returned.", data.size)
            data
        }

    override fun getUpdatedResources(): List<DigitalEnhetResource> = emptyList()

    companion object {
        private val log = LoggerFactory.getLogger(DigitalEnhetRepository::class.java)
    }
}
