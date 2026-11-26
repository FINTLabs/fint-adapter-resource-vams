package no.fintlabs.fintadapterresourcevams.model.vams

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls

class ResourceCollection<T> {
    @JsonProperty("_embedded")
    @field:JsonSetter(nulls = Nulls.AS_EMPTY)
    val embedded = Embedded<T>()

    fun <R> unwrap(mapper: (T) -> R): List<R> = embedded.entries.map(mapper)
}

class Embedded<T> {
    @JsonProperty("_entries")
    @field:JsonSetter(nulls = Nulls.AS_EMPTY)
    val entries: List<T> = emptyList()
}
