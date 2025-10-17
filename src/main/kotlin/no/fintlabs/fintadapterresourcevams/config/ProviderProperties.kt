package no.fintlabs.fintadapterresourcevams.config

import no.fintlabs.adapter.models.AdapterCapability
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "fint.provider")
data class ProviderProperties (
    //TODO? kanskje hardkode, may be no biggie 🤷
    val baseUrl: String = "https://beta.felleskomponent.no/provider",
    val orgId: String = "fintlabs.no",
    val heartbeatIntervalInMinutes: Byte = 3,
    val capabilities: List<AdapterCapability> = listOf(),
) {
    val adapterId = "https://$orgId/ressurs"
}