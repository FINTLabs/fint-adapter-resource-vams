package no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategori
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class EnhetstypeRepository(
    private val vamsClient: VamsClient,
) : ResourceRepository<EnhetstypeResource> {
    override fun getResources(): List<EnhetstypeResource> =
        runBlocking {
            val data =
                vamsClient
                    .getRequestData("kodeverk/maskinvarekategori/", MaskinvareKategori::class.java)
                    .unwrap { it.toFintModel() }
                    .filterNotNull()
            log.info("Fetching Enhetstype from Vams, {} resources returned.", data.size)
            data
        }

    override fun getUpdatedResources(): List<EnhetstypeResource> = emptyList()

    companion object {
        private val log = LoggerFactory.getLogger(EnhetstypeRepository::class.java)
    }
}
