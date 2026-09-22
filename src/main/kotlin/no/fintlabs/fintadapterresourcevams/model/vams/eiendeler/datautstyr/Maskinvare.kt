package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr

import no.fintlabs.fintadapterresourcevams.model.vams.getIdentifikatorLinkOrNull
import no.fintlabs.fintadapterresourcevams.model.vams.getNonBlankLinkOrNull
import no.fintlabs.fintadapterresourcevams.model.vams.getSystemIdLinkOrNull
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource
import org.slf4j.LoggerFactory

data class Maskinvare(
    val dataobjektId: Identifikator? = null,
    val navn: String? = null,
    val serienummer: String? = null,
    val systemId: Identifikator? = null,
) : FintLinks {
    private val linkMap = createLinks()

    override fun getLinks(): Map<String, List<Link>> = linkMap

    fun toFintModel(): DigitalEnhetResource? {
        val validSystemId = systemId?.takeIf { !it.identifikatorverdi.isNullOrBlank() }
        val validSerienummer = serienummer?.takeIf { it.isNotBlank() }
        val administrator = links.getNonBlankLinkOrNull("virksomhet")
        val enhetstype = links.getIdentifikatorLinkOrNull("maskinvarekategori", EnhetstypeResource::class.java, "kode")
        val plattform = links.getSystemIdLinkOrNull("plattform", PlattformResource::class.java)
        val status = links.getSystemIdLinkOrNull("status", StatusResource::class.java)

        if (validSystemId == null || validSerienummer == null || administrator == null || enhetstype == null || plattform == null) {
            log.warn(
                "Skipping invalid Maskinvare from VAMS: systemId={}, serienummer={}",
                systemId?.identifikatorverdi,
                serienummer,
            )
            return null
        }

        return DigitalEnhetResource().apply {
            dataobjektId = this@Maskinvare.dataobjektId
            navn = this@Maskinvare.navn
            serienummer = validSerienummer
            systemId = validSystemId
            addAdministrator(administrator)
            addEnhetstype(enhetstype)
            addPlattform(plattform)
            status?.let { addStatus(it) }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(Maskinvare::class.java)
    }
}
