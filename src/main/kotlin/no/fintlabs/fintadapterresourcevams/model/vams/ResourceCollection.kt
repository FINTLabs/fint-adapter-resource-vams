package no.fintlabs.fintadapterresourcevams.model.vams

import com.fasterxml.jackson.annotation.JsonProperty
import no.fint.model.resource.FintLinks

class ResourceCollection<T: FintLinks> {
    @JsonProperty("_embedded")
    val embedded = Embedded<T>()
}

class Embedded<T: FintLinks> {
    @JsonProperty("_entries")
    val entries: List<T> = emptyList()
}