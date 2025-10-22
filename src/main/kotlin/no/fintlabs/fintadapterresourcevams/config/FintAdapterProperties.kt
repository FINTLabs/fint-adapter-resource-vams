package no.fintlabs.fintadapterresourcevams.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "fint.adapter")
data class FintAdapterProperties (
    val username: String = "34234423",
    val password: String = "34234423",
    val clientId: String = "34234423",
    val clientSecret: String,
)