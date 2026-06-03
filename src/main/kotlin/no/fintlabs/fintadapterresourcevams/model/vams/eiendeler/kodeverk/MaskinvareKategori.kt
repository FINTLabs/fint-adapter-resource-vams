package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource

data class MaskinvareKategori (
    val kode: Identifikator?,
    val navn: String?,
    val systemId: Identifikator = Identifikator()
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links

    fun toFintModel(): EnhetstypeResource {
        val fintKode = kode?.identifikatorverdi.toString()
        val fintName = navn
        val id = systemId
        return EnhetstypeResource().apply {
            kode = fintKode
            systemId = id
            navn = fintName
        }
    }
}