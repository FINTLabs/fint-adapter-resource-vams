package no.fintlabs.fintadapterresourcevams.unit

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.fintlabs.fintadapterresourcevams.model.vams.ResourceCollection
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppeMedlemsskap
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppering
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.Maskinvare
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategori
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.Plattform
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.Status
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ModelConvertionTests {
    @Test
    fun `maskinvare toFintModel maps complete records and skips partial records`() {
        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/MaskinvareMock.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<Maskinvare> = jacksonObjectMapper().readValue(jsonString)
        val maskinvare = json.embedded.entries

        assertEquals(2, maskinvare.size)

        val completeRecord: DigitalEnhetResource = requireNotNull(maskinvare[0].toFintModel())
        assertEquals("3d4abd58-84b6-46a6-9d45-dae7861116e7", completeRecord.dataobjektId.identifikatorverdi)
        assertEquals("OPCPF17SPGD", completeRecord.navn)
        assertEquals("PF17SPGD", completeRecord.serienummer)
        assertEquals("13324", completeRecord.systemId.identifikatorverdi)
        assertEquals(
            "https://api.felleskomponent.no/administrasjon/organisasjon/organisasjonselement/organisasjonskode/F40.44.13",
            completeRecord.administrator.single().href,
        )
        assertEquals("\${ressurs.kodeverk.enhetstype}/kode/1", completeRecord.enhetstype.single().href)
        assertEquals("\${ressurs.kodeverk.plattform}/systemid/1000", completeRecord.plattform.single().href)
        assertEquals("\${ressurs.kodeverk.status}/systemid/9", completeRecord.status.single().href)

        assertNull(maskinvare[1].toFintModel())
    }

    @Test
    fun `maskinGruppering toFintModel maps singular mock`() {
        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/MaskinGrupperingMock.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<MaskinGruppering> = jacksonObjectMapper().readValue(jsonString)
        val maskingruppering: MaskinGruppering =
            json.embedded.entries
                .iterator()
                .next()

        val enhetsgruppe: EnhetsgruppeResource = requireNotNull(maskingruppering.toFintModel())
        assertEquals("MaskinGruppe_", enhetsgruppe.navn)
        assertEquals("1234", enhetsgruppe.systemId.identifikatorverdi)
        assertEquals(
            "https://api.felleskomponent.no/administrasjon/organisasjon/organisasjonselement/organisasjonskode/123",
            enhetsgruppe.organisasjonsenhet.single().href,
        )
        assertEquals("\${ressurs.kodeverk.enhetstype}/kode/1", enhetsgruppe.enhetstype.single().href)
        assertEquals("\${ressurs.kodeverk.plattform}/systemid/1000", enhetsgruppe.plattform.single().href)
    }

    @Test
    fun `maskinGruppering toFintModel returns null for records missing mandatory links`() {
        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/MaskinGrupperingSample.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<MaskinGruppering> = jacksonObjectMapper().readValue(jsonString)
        val maskingruppering = json.embedded.entries

        assertEquals(3, maskingruppering.size)

        assertNull(maskingruppering[0].toFintModel())
        assertNull(maskingruppering[1].toFintModel())
        assertNull(maskingruppering[2].toFintModel())
    }

    @Test
    fun `maskinGruppeMedlemsskap toFintModel maps real shape`() {
        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/MedlemskapSample.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<MaskinGruppeMedlemsskap> = jacksonObjectMapper().readValue(jsonString)
        val medlemskap = json.embedded.entries

        assertEquals(2, medlemskap.size)

        val firstMembership: EnhetsgruppemedlemskapResource = requireNotNull(medlemskap[0].toFintModel())
        assertEquals("1080_13324", firstMembership.systemId.identifikatorverdi)
        assertEquals("\${ressurs.datautstyr.enhetsgruppe}/systemid/1080", firstMembership.enhetsgruppe.single().href)
        assertEquals("\${ressurs.datautstyr.digitalenhet}/systemid/13324", firstMembership.digitalEnhet.single().href)
    }

    @Test
    fun `maskinvareKategori toFintModel maps singular mock`() {
        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/MaskinvareKategori.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<MaskinvareKategori> = jacksonObjectMapper().readValue(jsonString)
        val maskinvarekategorier = json.embedded.entries

        assertEquals(5, maskinvarekategorier.size)

        val enhetstype: EnhetstypeResource = requireNotNull(maskinvarekategorier[0].toFintModel())
        assertEquals("1", enhetstype.kode)
        assertEquals("PC", enhetstype.navn)
        assertEquals("5", enhetstype.systemId.identifikatorverdi)
        assertNull(enhetstype.passiv)

        val lastEnhetstype: EnhetstypeResource = requireNotNull(maskinvarekategorier[4].toFintModel())
        assertEquals("5", lastEnhetstype.kode)
        assertEquals("Tynnklient", lastEnhetstype.navn)
        assertEquals("1075", lastEnhetstype.systemId.identifikatorverdi)
    }

    @Test
    fun `status toFintModel maps real shape`() {
        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/StatusSample.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<Status> = jacksonObjectMapper().readValue(jsonString)
        val statuses = json.embedded.entries

        assertEquals(2, statuses.size)

        val status: StatusResource = requireNotNull(statuses[0].toFintModel())
        assertEquals("HARDWARE_STATUS", status.kode)
        assertEquals("I bruk", status.navn)
        assertEquals("9", status.systemId.identifikatorverdi)
        assertNull(status.passiv)
    }

    @Test
    fun `plattform toFintModel maps real shape`() {
        val path = Paths.get("src/test/kotlin/no/fintlabs/fintadapterresourcevams/resources/PlattformSample.json")
        val jsonString = path.toFile().readText()
        val json: ResourceCollection<Plattform> = jacksonObjectMapper().readValue(jsonString)
        val plattformer = json.embedded.entries

        assertEquals(3, plattformer.size)

        val plattform: PlattformResource = requireNotNull(plattformer[2].toFintModel())
        assertEquals("WIN", plattform.kode)
        assertEquals("Windows", plattform.navn)
        assertEquals("1000", plattform.systemId.identifikatorverdi)
        assertNull(plattform.passiv)
    }
}
