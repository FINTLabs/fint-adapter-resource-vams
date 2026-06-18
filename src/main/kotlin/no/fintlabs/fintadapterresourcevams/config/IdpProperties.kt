package no.fintlabs.fintadapterresourcevams.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "idp.url")
data class IdpProperties (
    val vamsIdp: String,
)