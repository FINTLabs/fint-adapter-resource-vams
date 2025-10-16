package no.fintlabs.fintadapterresourcevams.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class FintIdpResponse (
    @JsonProperty("access_token")
    val accessToken: String,
)