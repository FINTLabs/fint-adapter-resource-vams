package no.fintlabs.fintadapterresourcevams.model.vams.TEMP

import no.fintlabs.fintadapterresourcevams.VamsClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.RestController

@RestController
class DebugController(
    private val vamsClient: VamsClient
) {

    @Scheduled(fixedRate = 15_000)
    suspend fun getVAMSData() = vamsClient.getRequestData("/kodeverk/maskinvarekategori")
}