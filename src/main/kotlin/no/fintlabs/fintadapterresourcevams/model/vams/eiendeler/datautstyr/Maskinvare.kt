package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.resource.FintLinks
import no.fint.model.resource.Link
import no.fintlabs.fintadapterresourcevams.model.vams.Timestamp
import no.fintlabs.fintadapterresourcevams.model.vams.maskinvareNavn

data class Maskinvare(
    val serienummer: String,
    val systemId: Identifikator = Identifikator(),
    val navn: maskinvareNavn,
    val timestamp: Timestamp
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links
}
