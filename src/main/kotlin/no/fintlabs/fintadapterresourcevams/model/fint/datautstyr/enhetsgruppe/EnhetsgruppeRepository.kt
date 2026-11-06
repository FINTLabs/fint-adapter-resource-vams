package no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppe

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype.EnhetstypeRepository
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppering
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategoriReferences
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class EnhetsgruppeRepository(
    private val vamsClient: VamsClient,
    private val enhetstypeRepository: EnhetstypeRepository,
) : ResourceRepository<EnhetsgruppeResource> {
    override fun getResources(): List<EnhetsgruppeResource> =
        runBlocking {
            val categoryReferences = MaskinvareKategoriReferences(enhetstypeRepository.getCategories())
            val data =
                vamsClient
                    .getRequestData("datautstyr/maskingruppering/", MaskinGruppering::class.java)
                    .unwrap { it.toFintModel(categoryReferences) }
                    .filterNotNull()
            log.info("Fetching Enhetsgruppe from Vams, {} resources returned.", data.size)
            data
        }

    override fun getUpdatedResources(): List<EnhetsgruppeResource> = emptyList()

    companion object {
        private val log = LoggerFactory.getLogger(EnhetsgruppeRepository::class.java)
    }
}
