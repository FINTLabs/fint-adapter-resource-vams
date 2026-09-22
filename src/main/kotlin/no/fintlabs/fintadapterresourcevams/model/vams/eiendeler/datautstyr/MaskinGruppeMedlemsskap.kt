package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr

import no.fintlabs.fintadapterresourcevams.model.vams.getSystemIdLinkOrNull
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource
import org.slf4j.LoggerFactory

data class MaskinGruppeMedlemsskap(
    val systemId: Identifikator? = null,
) : FintLinks {
    private val linkMap = createLinks()

    override fun getLinks(): Map<String, List<Link>> = linkMap

    fun toFintModel(): EnhetsgruppemedlemskapResource? {
        val validSystemId = systemId?.takeIf { !it.identifikatorverdi.isNullOrBlank() }
        val group = links.getSystemIdLinkOrNull("maskingruppering", EnhetsgruppeResource::class.java)
        val digitalEnhet = links.getSystemIdLinkOrNull("maskinvare", DigitalEnhetResource::class.java)

        if (validSystemId == null || group == null || digitalEnhet == null) {
            log.warn(
                "Skipping invalid MaskinGruppeMedlemsskap from VAMS: systemId={}",
                systemId?.identifikatorverdi,
            )
            return null
        }

        return EnhetsgruppemedlemskapResource().apply {
            systemId = validSystemId
            addEnhetsgruppe(group)
            addDigitalEnhet(digitalEnhet)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MaskinGruppeMedlemsskap::class.java)
    }
}
