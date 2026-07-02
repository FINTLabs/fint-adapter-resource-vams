package no.fintlabs.fintadapterresourcevams.model.vams.TEMP

import kotlinx.coroutines.runBlocking
import no.fintlabs.fintadapterresourcevams.VamsClient
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategori
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.RestController

@RestController
class DebugController(
    private val vamsClient: VamsClient
) {

    @Scheduled(fixedDelay = 6000)
    fun getMaskinVareKategoriData() = runBlocking {
            vamsClient.getRequestData<MaskinvareKategori>(
            "kodeverk/maskinvarekategori",
        )
    }

}