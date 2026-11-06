package no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk

import no.fintlabs.fintadapterresourcevams.model.vams.getNonBlankLinkOrNull
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource

class MaskinvareKategoriReferences(
    categories: List<MaskinvareKategori>,
) {
    private val validCategories = categories.filter { !it.kode.isNullOrBlank() && !it.navn.isNullOrBlank() }
    private val systemIds = validCategories.mapNotNull { it.systemId?.identifikatorverdi?.takeIf(String::isNotBlank) }.toSet()
    private val systemIdByKode =
        validCategories.associate { category ->
            val kode = requireNotNull(category.kode)
            kode to (category.systemId?.identifikatorverdi?.takeIf(String::isNotBlank) ?: kode)
        }

    fun resolve(
        links: Map<String, List<Link>>,
        relation: String,
    ): Link? {
        val value = links.getNonBlankLinkOrNull(relation)?.href?.substringAfterLast("/") ?: return null
        val systemId =
            when {
                // VAMS labels category codes as systemId in links. Codes take priority when values overlap.
                value in systemIdByKode -> systemIdByKode.getValue(value)

                value in systemIds -> value

                else -> return null
            }
        return Link.with(EnhetstypeResource::class.java, "systemid", systemId)
    }
}
