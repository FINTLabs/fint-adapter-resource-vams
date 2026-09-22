package no.fintlabs.fintadapterresourcevams.model.fint

import kotlinx.coroutines.runBlocking
import no.fintlabs.adapter.config.AdapterProperties
import no.fintlabs.adapter.datasync.ResourcePublisher
import no.fintlabs.adapter.datasync.ResourceRepository
import no.fintlabs.adapter.datasync.SyncData
import no.fintlabs.adapter.models.AdapterCapability
import no.fintlabs.adapter.models.sync.SyncType
import no.novari.fint.model.resource.FintLinks
import org.slf4j.LoggerFactory

abstract class VamsResourcePublisher<T : FintLinks>(
    repository: ResourceRepository<T>,
    props: AdapterProperties,
    private val resourceKey: String,
    val resourceDisplayName: String,
) : ResourcePublisher<T, ResourceRepository<T>>(repository, props) {
    private val log = LoggerFactory.getLogger(javaClass)

    private fun publishFullSync() =
        runBlocking {
            log.info("Starting full sync of {}", resourceDisplayName)
            val resources = repository.getResources()
            submit(SyncData(resources, SyncType.FULL))
            log.info(
                "Completed full sync of {}. Submitted {} resources.",
                resourceDisplayName,
                resources.size,
            )
        }

    override fun doFullSync() {
        publishFullSync()
    }

    override fun doDeltaSync() {
        // NOT SUPPORTED
    }

    override fun getCapability(): AdapterCapability? = adapterProperties.getCapabilityByResource(resourceKey)
}
