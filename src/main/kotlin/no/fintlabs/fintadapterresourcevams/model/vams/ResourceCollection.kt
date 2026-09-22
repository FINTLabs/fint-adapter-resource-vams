package no.fintlabs.fintadapterresourcevams.model.vams

import com.fasterxml.jackson.annotation.JsonProperty

class ResourceCollection<T> {
    @JsonProperty("_embedded")
    val embedded = Embedded<T>()

    fun <R> unwrap(mapper: (T) -> R): List<R> = embedded.entries.map(mapper)
}

class Embedded<T> {
    @JsonProperty("_entries")
    val entries: List<T> = emptyList()
}
