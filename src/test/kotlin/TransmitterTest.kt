import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.transmitter.ApiProxy
import be.compuwave.peppol_box_transmitter.transmitter.Transmitter
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openapitools.client.apis.PeppolBoxByFlexinaAPIApi
import org.openapitools.client.infrastructure.ClientException
import utils.provideValidConfig
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TransmitterTest {
	
	@BeforeEach
	fun setUp() {
		
		mockkObject(AppConfig)
		every { AppConfig.config } returns provideValidConfig()
		
		mockkObject(ApiProxy)
	}
	
	@AfterEach
	fun tearDown() {
		unmockkAll()
	}
	
	@Test
	fun `document successfully sent`() {
		
		// arrange
		val file = File("src/test/resources/files/file1.xml")
		every { ApiProxy.client } returns mockk<PeppolBoxByFlexinaAPIApi>(relaxed = true)
		
		// act
		val res = Transmitter.sendDocument(file)
		
		// assert
		assertTrue(res.isSuccess)
	}
	
	@Test
	fun `document NOT successfully sent`() {
		
		// arrange
		val file = File("src/test/resources/files/file1.xml")
		every { ApiProxy.client } throws ClientException("Something went wrong")
		
		// act
		val res = Transmitter.sendDocument(file)
		
		// assert
		assertTrue(res.isFailure)
	}
	
	@Test
	fun `documents sent`() {
		
		// arrange
		val files = setOf(
			File("src/test/resources/files/file1.xml"),
			File("src/test/resources/files/file2.xml"),
			File("src/test/resources/files/file3.xml")
		)
		every { ApiProxy.client } returns mockk<PeppolBoxByFlexinaAPIApi>(relaxed = true)
		
		// act
		val res = Transmitter.sendDocuments(files)
		
		// assert
		assertEquals(files.size, res.size)
	}
	
	@Test
	fun `documents sent failure`() {
		
		// arrange
		val files = setOf(
			File("src/test/resources/files/file1.xml"),
			File("src/test/resources/files/file2.xml"),
			File("src/test/resources/files/file3.xml")
		)
		every { ApiProxy.client } throws ClientException("Something went wrong")
		
		// act
		val res = Transmitter.sendDocuments(files)
		
		// assert
		assertEquals(0, res.size)
	}
}
