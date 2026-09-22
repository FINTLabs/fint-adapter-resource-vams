package no.fintlabs.fintadapterresourcevams.model.fint

import no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.digitalEnhet.DigitalEnhetPublisher
import no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppe.EnhetsgruppePublisher
import no.fintlabs.fintadapterresourcevams.model.fint.datautstyr.enhetsgruppemedlemskap.EnhetsgruppeMedlemskapPublisher
import no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.enhetstype.EnhetstypePublisher
import no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.plattform.PlattformPublisher
import no.fintlabs.fintadapterresourcevams.model.fint.kodeverk.status.StatusPublisher
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class VamsFullSyncJob(
    statusPublisher: StatusPublisher,
    plattformPublisher: PlattformPublisher,
    enhetstypePublisher: EnhetstypePublisher,
    enhetsgruppePublisher: EnhetsgruppePublisher,
    digitalEnhetPublisher: DigitalEnhetPublisher,
    enhetsgruppeMedlemskapPublisher: EnhetsgruppeMedlemskapPublisher,
) {
    private val publishers: List<VamsResourcePublisher<*>> =
        listOf(
            statusPublisher,
            plattformPublisher,
            enhetstypePublisher,
            enhetsgruppePublisher,
            digitalEnhetPublisher,
            enhetsgruppeMedlemskapPublisher,
        )

    @Scheduled(cron = VAMS_FULL_SYNC_CRON)
    fun performFullSync() {
        log.info("Starting scheduled VAMS full sync for {} resources", publishers.size)

        publishers.forEachIndexed { index, publisher ->
            if (index > 0) {
                log.info(
                    "Waiting {} seconds before starting {} full sync",
                    DELAY_BETWEEN_FULL_SYNCS.seconds,
                    publisher.resourceDisplayName,
                )
                Thread.sleep(DELAY_BETWEEN_FULL_SYNCS.toMillis())
            }

            log.info("Starting {} full sync", publisher.resourceDisplayName)
            publisher.doFullSync()
        }

        log.info("Completed scheduled VAMS full sync")
    }

    companion object {
        private val log = LoggerFactory.getLogger(VamsFullSyncJob::class.java)
        private val DELAY_BETWEEN_FULL_SYNCS: Duration = Duration.ofMinutes(1)
    }
}
