package no.fintlabs.fintadapterresourcevams.model.vams

import no.novari.fint.model.resource.Link

fun Map<String, List<Link>>.getLinkOrNull(relation: String): Link? = this[relation]?.firstOrNull()

fun Map<String, List<Link>>.getNonBlankLinkOrNull(relation: String): Link? =
    getLinkOrNull(relation)?.takeIf { link ->
        !link.href.isNullOrBlank() && link.href.substringAfterLast("/").isNotBlank()
    }

fun Map<String, List<Link>>.getIdentifikatorLinkOrNull(
    relation: String,
    resourceClass: Class<*>,
    identifikatorName: String,
): Link? =
    getNonBlankLinkOrNull(relation)
        ?.href
        ?.substringAfterLast("/")
        ?.let { Link.with(resourceClass, identifikatorName, it) }

fun Map<String, List<Link>>.getSystemIdLinkOrNull(
    relation: String,
    resourceClass: Class<*>,
): Link? = getIdentifikatorLinkOrNull(relation, resourceClass, "systemid")
