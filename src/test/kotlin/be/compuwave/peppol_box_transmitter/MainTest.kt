package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.transmitter.ApiProxy
import be.compuwave.peppol_box_transmitter.utils.print
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openapitools.client.apis.PeppolBoxByFlexinaAPIApi
import java.io.File
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainTest {
	
	companion object {
		val targetFolder = File("target/test")
		val filesFolder = File(targetFolder, "files")
		val sentFolder = File(filesFolder, "sent")
		val loggingFolder = File(filesFolder, "logs")
		val downloadFolder = File(filesFolder, "download")
		val propertyFile = File(targetFolder, "valid.properties")
	}
	
	
	@BeforeEach
	fun setUp() {
		
		targetFolder.deleteRecursively()
		
		// mock object
		mockkObject(AppConfig)
		every { AppConfig.config } returns provideValidConfig()
		
		val apiClient = mockk<PeppolBoxByFlexinaAPIApi>(relaxed = true)
		mockkObject(ApiProxy)
		every { ApiProxy.client } returns apiClient
		
		// mock for download process
		val doc1 = File.createTempFile("doc-1", ".xml").also { it.writeText("<root><id>doc-1</id></root>") }
		val doc2 = File.createTempFile("doc-2", ".xml").also { it.writeText("<root><id>doc-2</id></root>") }
		val doc3 = File.createTempFile("doc-3", ".xml").also { it.writeText("<root><id>doc-3</id></root>") }
		
		every { apiClient.listInboundDocuments(any()) } returns listOf(
			mapOf("id" to "doc-1"),
			mapOf("id" to "doc-2"),
			mapOf("id" to "doc-3")
		)
		every { apiClient.downloadInboundDocumentXml("doc-1") } returns doc1
		every { apiClient.downloadInboundDocumentXml("doc-2") } returns doc2
		every { apiClient.downloadInboundDocumentXml("doc-3") } returns doc3
		
		// copy test files to target folder
		val originalFolder = File("src/test/resources/files")
		originalFolder.copyRecursively(filesFolder, overwrite = true)
		
		// copy property file
		val originalPropertyFile = File("src/test/resources/properties/valid.properties")
		originalPropertyFile.copyTo(propertyFile, overwrite = true)
		
		// create log folder
		loggingFolder.mkdirs()
		
		// create download folder
		downloadFolder.mkdirs()
	}
	
	@AfterEach
	fun tearDown() {
		
		// release mock
		unmockkAll()
		
		// delete target folder
		targetFolder.deleteRecursively()
	}
	
	@Test
	fun `all sent process`() {
		
		val arguments = arrayOf("--properties=target/test/valid.properties", "--action=send")
		main(arguments)
		
		assertTrue(propertyFile.exists())
		assertEquals(1, loggingFolder.listFiles()?.size)
		assertEquals(1, filesFolder.listFiles()?.filter { it.isFile }?.size)
		assertEquals(3, sentFolder.listFiles()?.size)
	}
	
	@Test
	fun `all download process`() {
		
		val arguments = arrayOf("--properties=target/test/valid.properties", "--action=download")
		main(arguments)
		
		assertTrue(propertyFile.exists())
		assertEquals(3, downloadFolder.listFiles()?.size)
		
		val properties = Properties().also { it.load(propertyFile.inputStream()) }
		val downloadFrom = properties.getProperty("DOWNLOAD_FROM")
		val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
		assertTrue(downloadFrom.startsWith(today.print()), "DOWNLOAD_FROM should be updated to today's date")
	}
}
