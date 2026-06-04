package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr


import no.fintlabs.fintadapterresourcevams.model.vams.getLinkOrNull
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource

data class Maskinvare(
    val dataobjektId: Identifikator?,
    val navn: String?,
    val serienummer: String,
    val systemId: Identifikator = Identifikator(),
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links

    fun toFintModel(): DigitalEnhetResource {
        val administrator = links.getLinkOrNull("virksomhet")
        val enhetstype = links.getLinkOrNull("enhetstype")
        val plattform = links.getLinkOrNull("plattform")
        val status = links.getLinkOrNull("status")

        return DigitalEnhetResource().apply {
            dataobjektId = this.dataobjektId
            navn = this.navn
            serienummer = this.serienummer
            systemId = this.systemId
            addAdministrator(administrator)
            addEnhetstype(enhetstype)
            addPlattform(plattform)
            addStatus(status)
        }
    }
}