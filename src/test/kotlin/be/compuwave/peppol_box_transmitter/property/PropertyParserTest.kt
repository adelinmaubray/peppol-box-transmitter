package be.compuwave.peppol_box_transmitter.property

import be.compuwave.peppol_box_transmitter.utils.DATE_FORMAT
import be.compuwave.peppol_box_transmitter.utils.print
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.assertDoesNotThrow
import org.valiktor.ConstraintViolationException
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PropertyParserTest {
	
	@Test
	fun `load valid properties file`() {
		assertDoesNotThrow {
			PropertyParser.loadProperties(File("src/test/resources/properties/valid.properties"))
		}
	}

	@Test
	fun `load invalid properties file`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			PropertyParser.loadProperties(File("src/test/resources/properties/invalid.properties"))
		}
		
		assertEquals(6, exception.constraintViolations.size)
		assertEquals(
			listOf("NotBlank", "NotBlank", "Website", "NotBlank", "NotBlank", "NotBlank"),
			exception.constraintViolations.map { it.constraint.name })
		assertEquals(
			listOf("inputDirectory", "downloadDirectory", "baseUrl", "tenantId", "apiKey", "apiSecret"),
			exception.constraintViolations.map { it.property })
	}
	
	@Test
	fun `override DOWNLOAD_FROM updates the property in the file`() {
		
		// arrange
		val tempFile = File.createTempFile("test", ".properties")
		File("src/test/resources/properties/valid.properties").copyTo(tempFile, overwrite = true)
		val newDateTime = LocalDateTime.parse("2025-06-15 08:30:00", DATE_FORMAT)
		
		// act
		PropertyParser.overrideDownloadFrom(tempFile, newDateTime)
		
		// assert
		val properties = Properties()
		properties.load(FileInputStream(tempFile))
		assertEquals(newDateTime.print(), properties.getProperty(ApplicationProperty.DOWNLOAD_FROM.name))
		
		tempFile.delete()
	}
}
