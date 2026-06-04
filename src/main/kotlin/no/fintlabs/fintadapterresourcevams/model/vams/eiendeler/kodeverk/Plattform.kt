package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.felles.kompleksedatatyper.Periode
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource

data class Plattform(
    val gyldighetsperiode: Periode? = null,
    val systemId: Identifikator = Identifikator(),
    val kode: Identifikator?,
    val passiv: Boolean? = null,
    val navn: String?,
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links

    fun toFintModel(): PlattformResource {
        val periode = gyldighetsperiode
        val fintKode = kode?.identifikatorverdi.toString()
        val fintName = navn
        val passivStatus = passiv
        val id = systemId
        return PlattformResource().apply {
            gyldighetsperiode = periode
            kode = fintKode
            navn = fintName
            passiv = passivStatus
            systemId = id
        }
    }
}
