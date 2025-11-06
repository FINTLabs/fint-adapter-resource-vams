package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.resource.FintLinks
import no.fint.model.resource.Link

data class Operativsystem (
    val systemId: Identifikator = Identifikator(),
    val navn: String,
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links
}