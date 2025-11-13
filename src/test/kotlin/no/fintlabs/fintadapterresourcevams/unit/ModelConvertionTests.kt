package no.fintlabs.fintadapterresourcevams.unit

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import no.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import no.fintlabs.fintadapterresourcevams.model.vams.ResourceCollection
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppering
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.Maskinvare
import java.nio.file.Paths
import kotlin.test.Test

class ModelConvertionTests {

    @Test
    fun Test_Maskinvare_toFintModel_singular_mock () {
        // Importing and declaring the mock data.
        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/MaskinvareMock.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<Maskinvare> = jacksonObjectMapper().readValue(jsonString)
        val maskinvare: Maskinvare = json.embedded.entries.iterator().next()

        // Implimenting Maskinvare.toFintModel() function
        val digitalEnhet: DigitalEnhetResource = maskinvare.toFintModel()
        println(digitalEnhet.toString())
    }

    @Test
    fun Test_MaskinGruppering_toFintModel_singular_mock () {

        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/MaskinGrupperingMock.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<MaskinGruppering> = jacksonObjectMapper().readValue(jsonString)
        val maskingruppering: MaskinGruppering = json.embedded.entries.iterator().next()

        val enhetsgruppe: EnhetsgruppeResource = maskingruppering.toFintModel()
        println(enhetsgruppe.toString())

    }



}