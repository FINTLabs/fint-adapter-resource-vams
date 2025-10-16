package no.fintlabs.fintadapterresourcevams.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "fint.adapter")
data class FintAdapterProperties (
    val username: String,
    val password: String,
    val clientId: String,
    val clientSecret: String,
)