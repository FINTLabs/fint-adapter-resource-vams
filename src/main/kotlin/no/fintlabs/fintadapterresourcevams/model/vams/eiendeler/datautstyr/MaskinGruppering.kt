package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr

import no.fint.model.resource.FintLinks
import no.fintlabs.fintadapterresourcevams.model.vams.Timestamp
import no.fintlabs.fintadapterresourcevams.model.vams.getLinkOrNull
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource

data class MaskinGruppering (
    val navn: String,
    val systemId: Identifikator = Identifikator(),
    val timestamp: Timestamp
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links

    fun toFintModel(): EnhetsgruppeResource {
        val fintName = navn
        val id = systemId
        val org = _links.getLinkOrNull("virksomhet")
        val unitType = _links.getLinkOrNull("enhetstype")
        val platform = _links.getLinkOrNull("plattform")
        return EnhetsgruppeResource().apply {
            systemId = id
            navn = fintName
            addOrganisasjonsenhet(org)
            addEnhetstype(unitType)
            addPlattform(platform)
        }
    }
}