package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.resource.FintLinks
import no.fint.model.resource.Link
import no.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import no.fintlabs.fintadapterresourcevams.model.vams.Timestamp
import no.fintlabs.fintadapterresourcevams.model.vams.getLinkOrNull
import no.fintlabs.fintadapterresourcevams.model.vams.maskinvareNavn

data class Maskinvare(
    val serienummer: Identifikator = Identifikator(),
    val systemId: Identifikator = Identifikator(),
    val navn: maskinvareNavn,
    val timestamp: Timestamp
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links

    fun toFintModel(): DigitalEnhetResource {
        val fintName = "${navn.produsent}, ${navn.modell}, ${navn.modellspesifikasjon}"
        val itemSerialNumber = serienummer.identifikatorverdi
        val id = systemId
        val admin = links.getLinkOrNull("virksomhet")
        val unitType = links.getLinkOrNull("enhetstype")
        val platform = links.getLinkOrNull("plattform")
        val status = links.getLinkOrNull("status")
        return DigitalEnhetResource().apply {
            systemId = id
            serienummer = itemSerialNumber
            navn = fintName
            addAdministrator(admin)
            addEnhetstype(unitType)
            addPlattform(platform)
            addStatus(status)
        }
    }
}