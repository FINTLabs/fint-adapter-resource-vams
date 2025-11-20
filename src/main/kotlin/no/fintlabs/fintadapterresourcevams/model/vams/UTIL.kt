package no.fintlabs.fintadapterresourcevams.model.vams

import no.fint.model.resource.Link

fun Map<String, List<Link>>.getLinkOrNull(relation: String) =
    this.getOrDefault(relation, listOf()).firstOrNull()

// TODO : Several Links