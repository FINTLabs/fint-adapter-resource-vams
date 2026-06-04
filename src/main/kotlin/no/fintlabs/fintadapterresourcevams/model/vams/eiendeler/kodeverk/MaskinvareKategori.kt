package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.felles.kompleksedatatyper.Periode
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource

data class MaskinvareKategori (
    val gyldighetsperiode: Periode? = null,
    val kode: Identifikator = Identifikator(),
    val navn: String,
    val passiv : Boolean? = null,
    val systemId: Identifikator = Identifikator()
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links

    fun toFintModel(): EnhetstypeResource {
        val periode = gyldighetsperiode
        val fintKode = kode.identifikatorverdi.toString()
        val fintName = navn
        val passivStatus = passiv
        val id = systemId
        return EnhetstypeResource().apply {
            gyldighetsperiode = periode
            kode = fintKode
            navn = fintName
            passiv = passivStatus
            systemId = id
        }
    }
}