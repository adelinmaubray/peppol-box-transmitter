import be.compuwave.peppol_box_transmitter.config.ConfigModel
import org.junit.jupiter.api.assertDoesNotThrow
import org.valiktor.ConstraintViolationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConfigModelTest {
	
	companion object {
		private const val VALID_TEST_MODE = true
		private const val VALID_INPUT_DIRECTORY = "src/main/resources/input"
		private const val VALID_BASE_URL = "https://www.jetbrains.com"
	}
	
	@Test
	fun `config model is valid`() {
		assertDoesNotThrow {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				baseUrl = VALID_BASE_URL
			)
		}
	}
	
	@Test
	fun `input directory is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				baseUrl = VALID_BASE_URL,
				inputDirectory = ""
			)
		}
		
		assertEquals(ConfigModel::inputDirectory.name, exception.constraintViolations.first().property)
		assertEquals("NotBlank", exception.constraintViolations.first().constraint.name)
	}
	
	@Test
	fun `base url is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				baseUrl = ""
			)
		}
		
		assertEquals(ConfigModel::baseUrl.name, exception.constraintViolations.first().property)
		assertEquals("NotBlank", exception.constraintViolations.first().constraint.name)
	}
	
	@Test
	fun `base url is not a website`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				baseUrl = "dummy"
			)
		}
		
		assertEquals(ConfigModel::baseUrl.name, exception.constraintViolations.first().property)
		assertEquals("Website", exception.constraintViolations.first().constraint.name)
	}
}
