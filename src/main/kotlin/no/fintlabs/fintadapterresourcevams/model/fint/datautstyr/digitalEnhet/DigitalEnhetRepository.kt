package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype.EnhetstypeRepository
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.Maskinvare
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategoriReferences
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import tools.jackson.databind.json.JsonMapper

@Repository
class DigitalEnhetRepository(
    private val vamsClient: VamsClient,
    private val enhetstypeRepository: EnhetstypeRepository,
) : ResourceRepository<DigitalEnhetResource> {
    private val objectMapper = JsonMapper.builder().build()

    override fun getResources(): List<DigitalEnhetResource> =
        runBlocking {
            val categories = enhetstypeRepository.getCategories()
            log.info("Loaded {} MaskinvareKategori references for DigitalEnhet", categories.size)
            val categoryReferences = MaskinvareKategoriReferences(categories)
            val converted =
                vamsClient
                    .getRequestData("datautstyr/maskinvare/", Maskinvare::class.java)
                    .unwrap { maskinvare ->
                        val systemId = maskinvare.systemId?.identifikatorverdi
                        log.info("VAMS Maskinvare input systemId={} json={}", systemId, objectMapper.writeValueAsString(maskinvare))
                        maskinvare.toFintModel(categoryReferences).also { resource ->
                            if (resource == null) {
                                log.warn("VAMS Maskinvare conversion skipped systemId={}", systemId)
                            }
                        }
                    }
            val data = converted.filterNotNull()
            log.info("Fetching DigitalEnhet from Vams, {} resources returned.", data.size)
            data
        }

    override fun getUpdatedResources(): List<DigitalEnhetResource> = emptyList()

    companion object {
        private val log = LoggerFactory.getLogger(DigitalEnhetRepository::class.java)
    }
}
