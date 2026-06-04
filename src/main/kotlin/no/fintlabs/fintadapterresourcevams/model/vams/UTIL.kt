package no.fintlabs.fintadapterresourcevams.model.vams

import no.novari.fint.model.resource.Link

fun Map<String, List<Link>>.getLinkOrNull(relation: String): Link? =
    this[relation]?.firstOrNull()

fun Map<String, List<Link>>.getListOfLinksOrNull(relation: String): List<Link>? =
    this[relation]?.takeIf { it.isNotEmpty() }