import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.main
import be.compuwave.peppol_box_transmitter.transmitter.ApiProxy
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openapitools.client.apis.PeppolBoxByFlexinaAPIApi
import utils.provideValidConfig
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainTest {
	
	companion object {
		val targetFolder = File("target/test")
		val filesFolder = File(targetFolder, "files")
		val sentFolder = File(filesFolder, "sent")
		val loggingFolder = File(filesFolder, "logs")
		val propertyFile = File(targetFolder, "valid.properties")
	}
	
	
	@BeforeEach
	fun setUp() {
		
		targetFolder.deleteRecursively()
		
		// mock object
		mockkObject(AppConfig)
		every { AppConfig.config } returns provideValidConfig()
		
		mockkObject(ApiProxy)
		every { ApiProxy.client } returns mockk<PeppolBoxByFlexinaAPIApi>(relaxed = true)
		
		// copy test files to target folder
		val originalFolder = File("src/test/resources/files")
		originalFolder.copyRecursively(filesFolder, overwrite = true)
		
		// copy property file
		val originalPropertyFile = File("src/test/resources/properties/valid.properties")
		originalPropertyFile.copyTo(propertyFile, overwrite = true)
		
		// create log folder
		loggingFolder.mkdirs()
	}
	
	@AfterEach
	fun tearDown() {
		
		// release mock
		unmockkAll()
		
		// delete target folder
		targetFolder.deleteRecursively()
	}
	
	@Test
	fun `all process`() {
		
		val arguments = arrayOf("--properties=target/test/valid.properties")
		main(arguments)
		
		assertTrue(propertyFile.exists())
		assertEquals(1, loggingFolder.listFiles()?.size)
		assertEquals(1, filesFolder.listFiles()?.filter { it.isFile }?.size)
		assertEquals(3, sentFolder.listFiles()?.size)
	}
}
