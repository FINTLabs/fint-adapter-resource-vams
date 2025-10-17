package no.fintlabs.fintadapterresourcevams.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "vams.client")
data class VamsClientProperties (
    val clientId: String,
    val clientSecret: String,
    val scope: String,
    val granttype: String,
    val apiToken: String,
    val countyCode: String,
)