package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr

import no.fintlabs.fintadapterresourcevams.model.vams.Timestamp
import no.fintlabs.fintadapterresourcevams.model.vams.getNonBlankLinkOrNull
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import org.slf4j.LoggerFactory

data class MaskinGruppering(
    val navn: String? = null,
    val systemId: Identifikator? = null,
    val timestamp: Timestamp? = null,
) : FintLinks {
    private val linkMap = createLinks()

    override fun getLinks(): Map<String, List<Link>> = linkMap

    fun toFintModel(): EnhetsgruppeResource? {
        val validSystemId = systemId?.takeIf { !it.identifikatorverdi.isNullOrBlank() }
        val validName = navn?.takeIf { it.isNotBlank() }
        val org = links.getNonBlankLinkOrNull("virksomhet")
        val enhetstype = Link.with(EnhetstypeResource::class.java, "systemid", TEST_ENHETSTYPE_SYSTEM_ID)
        val plattform = Link.with(PlattformResource::class.java, "systemid", TEST_PLATTFORM_SYSTEM_ID)

        if (validSystemId == null || validName == null || org == null) {
            log.warn(
                "Skipping invalid MaskinGruppering from VAMS: systemId={}, navn={}",
                systemId?.identifikatorverdi,
                navn,
            )
            return null
        }

        return EnhetsgruppeResource().apply {
            navn = validName
            systemId = validSystemId
            addOrganisasjonsenhet(org)
            addEnhetstype(enhetstype)
            addPlattform(plattform)
        }
    }

    companion object {
        private const val TEST_ENHETSTYPE_SYSTEM_ID = "87"
        private const val TEST_PLATTFORM_SYSTEM_ID = "1380"
        private val log = LoggerFactory.getLogger(MaskinGruppering::class.java)
    }
}
