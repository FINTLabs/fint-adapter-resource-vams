package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.resource.FintLinks
import no.fint.model.resource.Link

data class MaskinvareKategori (
    val kode: Identifikator?,
    val navn: String?,
    val systemId: Identifikator = Identifikator()
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links
}