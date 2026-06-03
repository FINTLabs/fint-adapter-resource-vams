package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr

import no.fintlabs.fintadapterresourcevams.model.vams.getLinkOrNull
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource

data class MaskinGruppeMedlemsskap(
    val systemId: Identifikator = Identifikator(),
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links

    fun toFintModel(): EnhetsgruppemedlemskapResource {
        val id = systemId
        val org = _links.getLinkOrNull("virksomhet")
        val unitType = _links.getLinkOrNull("enhetstype")
        return EnhetsgruppemedlemskapResource().apply {
            systemId = id
            addOrganisasjonsenhet(org)
            addEnhetstype(unitType)
        }
    }
}