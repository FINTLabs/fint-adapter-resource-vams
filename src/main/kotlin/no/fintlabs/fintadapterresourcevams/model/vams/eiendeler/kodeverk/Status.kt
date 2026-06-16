package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.felles.kompleksedatatyper.Periode
import no.novari.fint.model.resource.FintLinks
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource

data class Status(
    val gyldighetsperiode: Periode? = null,
    val systemId: Identifikator = Identifikator(),
    val kode: Identifikator = Identifikator(),
    val passiv: Boolean? = null,
    val navn: String?,
): FintLinks {
    val _links = this.createLinks()
    override fun getLinks(): Map<String, List<Link>> = _links

    fun toFintModel(): StatusResource {
        val periode = gyldighetsperiode
        val fintKode = kode.identifikatorverdi.toString()
        val fintName = navn
        val passivStatus = passiv
        val id = systemId
        return StatusResource().apply {
            gyldighetsperiode = periode
            systemId = id
            kode = fintKode
            passiv = passivStatus
            navn = fintName
        }
    }
}
