package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import org.slf4j.LoggerFactory

data class MaskinvareKategori(
    val systemId: Identifikator? = null,
    val kode: String? = null,
    val navn: String? = null,
) : FintLinks {
    private val linkMap = createLinks()

    override fun getLinks(): Map<String, List<Link>> = linkMap

    fun toFintModel(): EnhetstypeResource? {
        val validSystemId = systemId?.takeIf { !it.identifikatorverdi.isNullOrBlank() }
        val validKode = kode?.takeIf { it.isNotBlank() }
        val validName = navn?.takeIf { it.isNotBlank() }

        if (validSystemId == null || validKode == null || validName == null) {
            log.warn(
                "Skipping invalid MaskinvareKategori from VAMS: systemId={}, kode={}, navn={}",
                systemId?.identifikatorverdi,
                kode,
                navn,
            )
            return null
        }

        return EnhetstypeResource().apply {
            systemId = validSystemId
            kode = validKode
            navn = validName
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MaskinvareKategori::class.java)
    }
}
