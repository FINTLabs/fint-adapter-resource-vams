package no.fintlabs.fintadapterresourcevams

import no.fintlabs.fintadapterresourcevams.model.vams.ResourceCollection
import tools.jackson.databind.json.JsonMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ResourceCollectionTests {
    private val mapper = JsonMapper.builder().build()
    private val collectionType = mapper.typeFactory.constructParametricType(ResourceCollection::class.java, String::class.java)

    @Test
    fun `null entries become an empty collection`() {
        val collection = parse("""{"_embedded":{"_entries":null}}""")

        assertEquals(emptyList(), collection.embedded.entries)
        assertEquals(emptyList(), collection.unwrap { it.uppercase() })
    }

    @Test
    fun `null embedded object becomes an empty collection`() {
        val collection = parse("""{"_embedded":null}""")

        assertEquals(emptyList(), collection.embedded.entries)
        assertEquals(emptyList(), collection.unwrap { it.uppercase() })
    }

    @Test
    fun `missing collection fields remain empty`() {
        for (json in listOf("{}", """{"_embedded":{}}""")) {
            val collection = parse(json)

            assertEquals(emptyList(), collection.embedded.entries)
            assertEquals(emptyList(), collection.unwrap { it.uppercase() })
        }
    }

    @Test
    fun `populated collection maps all entries`() {
        val collection = parse("""{"_embedded":{"_entries":["first","second"]}}""")

        assertEquals(listOf("first", "second"), collection.embedded.entries)
        assertEquals(listOf("FIRST", "SECOND"), collection.unwrap { it.uppercase() })
    }

    @Test
    fun `invalid entries shape fails deserialization`() {
        assertFails { parse("""{"_embedded":{"_entries":{}}}""") }
    }

    private fun parse(json: String): ResourceCollection<String> = mapper.readValue(json, collectionType)
}
