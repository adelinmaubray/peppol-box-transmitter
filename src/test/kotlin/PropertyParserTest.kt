import be.compuwave.peppol_box_transmitter.property.PropertyParser
import org.junit.jupiter.api.assertDoesNotThrow
import org.valiktor.ConstraintViolationException
import java.io.File
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
		
		assertEquals(3, exception.constraintViolations.size)
		assertEquals(listOf("NotBlank", "NotBlank", "Website"), exception.constraintViolations.map { it.constraint.name })
		assertEquals(listOf("inputDirectory", "outputDirectory", "baseUrl"), exception.constraintViolations.map { it.property })
	}
}
