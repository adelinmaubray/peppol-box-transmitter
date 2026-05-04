package be.compuwave.peppol_box_transmitter.transmitter

import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.config.ConfigModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openapitools.client.apis.PeppolBoxByFlexinaAPIApi
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DownloaderTest {
	
	private val downloadDir = File("target/test-downloads")
	
	@BeforeEach
	fun setUp() {
		downloadDir.mkdirs()
		mockkObject(AppConfig)
		val config = mockk<ConfigModel>()
		every { config.downloadDirectory } returns downloadDir.absolutePath
		every { AppConfig.config } returns config
		
		mockkObject(ApiProxy)
	}
	
	@AfterEach
	fun tearDown() {
		unmockkAll()
		downloadDir.deleteRecursively()
	}
	
	@Test
	fun `test downloadDocuments with valid and invalid IDs`() {
		val mockClient = mockk<PeppolBoxByFlexinaAPIApi>()
		every { ApiProxy.client } returns mockClient
		
		val documents = listOf(
			mapOf("id" to "valid-id-1"),
			mapOf("id" to 123), // Invalid ID
			mapOf("id" to "valid-id-2")
		)
		
		every { mockClient.listInboundDocuments(any()) } returns documents
		
		// Mock downloadInboundDocumentXml to return a temporary file
		every { mockClient.downloadInboundDocumentXml(any()) } answers {
			val id = it.invocation.args[0] as String
			val file = File(downloadDir, "temp-$id.xml")
			file.writeText("<root><id>$id</id><content>some content</content></root>")
			file
		}
		
		Downloader.downloadDocuments(LocalDateTime(2023, 1, 1, 0, 0))
		
		val downloadedFiles = downloadDir.listFiles()?.filter { it.extension == "xml" } ?: emptyList()
		assertEquals(2, downloadedFiles.size, "Should have downloaded 2 valid documents")
		val fileNames = downloadedFiles.map { it.name }.toSet()
		assert(fileNames.contains("valid-id-1.xml"))
		assert(fileNames.contains("valid-id-2.xml"))
		
		// Check if XML is formatted (should start with <?xml and be pretty printed)
		downloadedFiles.forEach { file ->
			val content = file.readText()
			assertTrue(content.startsWith("<?xml"), "File ${file.name} should start with XML declaration")
			assertTrue(content.contains("\n"), "File ${file.name} should be pretty printed with new lines")
		}
	}
}
