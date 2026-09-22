package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import org.slf4j.LoggerFactory

data class Plattform(
    val systemId: Identifikator? = null,
    val kode: String? = null,
    val navn: String? = null,
) : FintLinks {
    private val linkMap = createLinks()

    override fun getLinks(): Map<String, List<Link>> = linkMap

    fun toFintModel(): PlattformResource? {
        val validSystemId = systemId?.takeIf { !it.identifikatorverdi.isNullOrBlank() }
        val validKode = kode?.takeIf { it.isNotBlank() }
        val validName = navn?.takeIf { it.isNotBlank() }

        if (validSystemId == null || validKode == null || validName == null) {
            log.warn(
                "Skipping invalid Plattform from VAMS: systemId={}, kode={}, navn={}",
                systemId?.identifikatorverdi,
                kode,
                navn,
            )
            return null
        }

        return PlattformResource().apply {
            systemId = validSystemId
            kode = validKode
            navn = validName
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(Plattform::class.java)
    }
}
