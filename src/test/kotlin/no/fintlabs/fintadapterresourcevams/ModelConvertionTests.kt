package no.fintlabs.fintadapterresourcevams

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.fintlabs.adapter.models.sync.SyncPageEntry
import no.fintlabs.fintadapterresourcevams.model.vams.ResourceCollection
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppeMedlemsskap
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.MaskinGruppering
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.datautstyr.Maskinvare
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategori
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.MaskinvareKategoriReferences
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.Plattform
import no.fintlabs.fintadapterresourcevams.model.vams.eiendeler.kodeverk.Status
import no.novari.fint.model.resource.ressurs.datautstyr.DigitalEnhetResource
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppeResource
import no.novari.fint.model.resource.ressurs.datautstyr.EnhetsgruppemedlemskapResource
import no.novari.fint.model.resource.ressurs.kodeverk.EnhetstypeResource
import no.novari.fint.model.resource.ressurs.kodeverk.PlattformResource
import no.novari.fint.model.resource.ressurs.kodeverk.StatusResource
import tools.jackson.databind.json.JsonMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ModelConvertionTests {
    @Test
    fun `production JSON mapper retains VAMS category links`() {
        val mapper = JsonMapper.builder().build()
        val categoryType = mapper.typeFactory.constructParametricType(ResourceCollection::class.java, MaskinvareKategori::class.java)
        val hardwareType = mapper.typeFactory.constructParametricType(ResourceCollection::class.java, Maskinvare::class.java)
        val categories: ResourceCollection<MaskinvareKategori> = mapper.readValue(fixture("MaskinvareKategoriSample.json"), categoryType)
        val hardware: ResourceCollection<Maskinvare> = mapper.readValue(fixture("MaskinvareMock.json"), hardwareType)

        val resource = requireNotNull(hardware.embedded.entries[0].toFintModel(MaskinvareKategoriReferences(categories.embedded.entries)))
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/5", resource.enhetstype.single().href)
    }

    @Test
    fun `maskinvare toFintModel maps complete records and skips partial records`() {
        val jsonString = fixture("MaskinvareMock.json")
        val json: ResourceCollection<Maskinvare> = jacksonObjectMapper().readValue(jsonString)
        val maskinvare = json.embedded.entries

        assertEquals(2, maskinvare.size)

        val completeRecord: DigitalEnhetResource = requireNotNull(maskinvare[0].toFintModel(categoryReferences()))
        assertEquals("3d4abd58-84b6-46a6-9d45-dae7861116e7", completeRecord.dataobjektId.identifikatorverdi)
        assertEquals("OPCPF17SPGD", completeRecord.navn)
        assertEquals("PF17SPGD", completeRecord.serienummer)
        assertEquals("13324", completeRecord.systemId.identifikatorverdi)
        assertEquals(
            "https://api.felleskomponent.no/administrasjon/organisasjon/organisasjonselement/organisasjonskode/F40.44.13",
            completeRecord.administrator.single().href,
        )
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/5", completeRecord.enhetstype.single().href)
        val publishedJson = jacksonObjectMapper().readTree(jacksonObjectMapper().writeValueAsString(completeRecord))
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/5", publishedJson["_links"]["enhetstype"][0]["href"].asText())
        val pageEntry = SyncPageEntry.of("13324", completeRecord)
        val webClientJson = jacksonObjectMapper().readTree(JsonMapper.builder().build().writeValueAsString(pageEntry))
        assertEquals("13324", webClientJson["identifier"].asText())
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/5", webClientJson["resource"]["_links"]["enhetstype"][0]["href"].asText())
        assertEquals("\${ressurs.kodeverk.plattform}/systemid/1000", completeRecord.plattform.single().href)
        assertEquals("\${ressurs.kodeverk.status}/systemid/9", completeRecord.status.single().href)

        assertNull(maskinvare[1].toFintModel(categoryReferences()))
    }

    @Test
    fun `maskinGruppering toFintModel maps singular mock`() {
        val jsonString = fixture("MaskinGrupperingMock.json")
        val json: ResourceCollection<MaskinGruppering> = jacksonObjectMapper().readValue(jsonString)
        val maskingruppering: MaskinGruppering =
            json.embedded.entries
                .iterator()
                .next()

        val enhetsgruppe: EnhetsgruppeResource = requireNotNull(maskingruppering.toFintModel(categoryReferences()))
        assertEquals("MaskinGruppe_", enhetsgruppe.navn)
        assertEquals("1234", enhetsgruppe.systemId.identifikatorverdi)
        assertEquals(
            "https://api.felleskomponent.no/administrasjon/organisasjon/organisasjonselement/organisasjonskode/123",
            enhetsgruppe.organisasjonsenhet.single().href,
        )
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/5", enhetsgruppe.enhetstype.single().href)
        assertEquals("\${ressurs.kodeverk.plattform}/systemid/1000", enhetsgruppe.plattform.single().href)
    }

    @Test
    fun `maskinGruppering toFintModel returns null for records missing mandatory links`() {
        val jsonString = fixture("MaskinGrupperingSample.json")
        val json: ResourceCollection<MaskinGruppering> = jacksonObjectMapper().readValue(jsonString)
        val maskingruppering = json.embedded.entries

        assertEquals(3, maskingruppering.size)

        assertNull(maskingruppering[0].toFintModel(categoryReferences()))
        assertNotNull(maskingruppering[1].toFintModel(categoryReferences()))
        assertNotNull(maskingruppering[2].toFintModel(categoryReferences()))
    }

    @Test
    fun `category links prefer code when a code and system ID collide`() {
        val json: ResourceCollection<Maskinvare> =
            jacksonObjectMapper().readValue(fixture("MaskinvareMock.json").replace("systemId/1", "systemId/5"))

        val resource = requireNotNull(json.embedded.entries[0].toFintModel(categoryReferences()))
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/1075", resource.enhetstype.single().href)
        val publishedJson = jacksonObjectMapper().readTree(jacksonObjectMapper().writeValueAsString(resource))
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/1075", publishedJson["_links"]["enhetstype"][0]["href"].asText())
    }

    @Test
    fun `category link still accepts an unambiguous system ID`() {
        val json: ResourceCollection<Maskinvare> =
            jacksonObjectMapper().readValue(fixture("MaskinvareMock.json").replace("systemId/1", "systemId/86"))

        val resource = requireNotNull(json.embedded.entries[0].toFintModel(categoryReferences()))
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/86", resource.enhetstype.single().href)
    }

    @Test
    fun `category code resolves to its system ID`() {
        val json: ResourceCollection<Maskinvare> =
            jacksonObjectMapper().readValue(fixture("MaskinvareMock.json").replace("systemId/1", "systemId/2"))

        val resource = requireNotNull(json.embedded.entries[0].toFintModel(categoryReferences()))
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/86", resource.enhetstype.single().href)
    }

    @Test
    fun `unknown category link does not create a dangling reference`() {
        val json: ResourceCollection<Maskinvare> =
            jacksonObjectMapper().readValue(fixture("MaskinvareMock.json").replace("systemId/1", "systemId/999"))

        assertNull(json.embedded.entries[0].toFintModel(categoryReferences()))
    }

    @Test
    fun `maskinGruppeMedlemsskap toFintModel maps real shape`() {
        val jsonString = fixture("MedlemskapSample.json")
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
        val jsonString = fixture("MaskinvareKategoriSample.json")
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
    fun `maskinvareKategori uses code as system ID when system ID is missing`() {
        val category = MaskinvareKategori(kode = "6", navn = "Annet")

        val resource = requireNotNull(category.toFintModel())
        assertEquals("6", resource.systemId.identifikatorverdi)
        assertEquals("6", resource.kode)

        val json: ResourceCollection<Maskinvare> =
            jacksonObjectMapper().readValue(fixture("MaskinvareMock.json").replace("systemId/1", "systemId/6"))
        val linkedResource = requireNotNull(json.embedded.entries[0].toFintModel(MaskinvareKategoriReferences(listOf(category))))
        assertEquals("\${ressurs.kodeverk.enhetstype}/systemid/6", linkedResource.enhetstype.single().href)
    }

    @Test
    fun `status toFintModel maps real shape`() {
        val jsonString = fixture("StatusSample.json")
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
        val jsonString = fixture("PlattformSample.json")
        val json: ResourceCollection<Plattform> = jacksonObjectMapper().readValue(jsonString)
        val plattformer = json.embedded.entries

        assertEquals(3, plattformer.size)

        val plattform: PlattformResource = requireNotNull(plattformer[2].toFintModel())
        assertEquals("WIN", plattform.kode)
        assertEquals("Windows", plattform.navn)
        assertEquals("1000", plattform.systemId.identifikatorverdi)
        assertNull(plattform.passiv)
    }

    private fun fixture(name: String): String =
        requireNotNull(javaClass.getResource("/fixtures/$name")) { "Missing test fixture: $name" }.readText()

    private fun categoryReferences(): MaskinvareKategoriReferences {
        val json: ResourceCollection<MaskinvareKategori> = jacksonObjectMapper().readValue(fixture("MaskinvareKategoriSample.json"))
        return MaskinvareKategoriReferences(json.embedded.entries)
    }
}
