package no.fintlabs.fintadapterresourcevams.model.vams

import com.fasterxml.jackson.annotation.JsonProperty
import no.novari.fint.model.resource.FintLinks

class ResourceCollection<T : FintLinks> {
    @JsonProperty("_embedded")
    val embedded = Embedded<T>()

    fun <R> unwrap(mapper: (T) -> R): List<R> {
        return embedded.entries.map(mapper)
    }
}

class Embedded<T: FintLinks> {
    @JsonProperty("_entries")
    val entries: List<T> = emptyList()
}