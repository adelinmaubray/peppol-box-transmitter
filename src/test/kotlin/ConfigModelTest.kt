import be.compuwave.peppol_box_transmitter.config.ConfigModel
import org.junit.jupiter.api.assertDoesNotThrow
import org.valiktor.ConstraintViolationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ConfigModelTest {
	
	companion object {
		private const val VALID_TEST_MODE = true
		private const val VALID_INPUT_DIRECTORY = "src/main/resources/input"
		private const val VALID_OUTPUT_DIRECTORY = "src/main/resources/output"
		private const val VALID_LOGGING_DIRECTORY = "src/main/resources/logs"
		private const val VALID_BASE_URL = "https://www.jetbrains.com"
		private const val VALID_TENANT_ID = "tenant-id"
		private const val VALID_API_KEY = "api-key"
		private const val VALID_API_SECRET = "api-secret"
	}
	
	@Test
	fun `config model is valid`() {
		assertDoesNotThrow {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				baseUrl = VALID_BASE_URL,
				tenantId = VALID_TENANT_ID,
				apiKey = VALID_API_KEY,
				apiSecret = VALID_API_SECRET
			).also {
				assertEquals(it.outputDirectory, "${it.inputDirectory}/sent")
				assertTrue(it.loggingDirectory.endsWith("/logs"))
			}
		}
		
		assertDoesNotThrow {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				outputDirectory = VALID_OUTPUT_DIRECTORY,
				loggingDirectory = VALID_LOGGING_DIRECTORY,
				baseUrl = VALID_BASE_URL,
				tenantId = VALID_TENANT_ID,
				apiKey = VALID_API_KEY,
				apiSecret = VALID_API_SECRET
			).also {
				assertEquals(it.outputDirectory, VALID_OUTPUT_DIRECTORY)
				assertEquals(it.loggingDirectory, VALID_LOGGING_DIRECTORY)
			}
		}
	}
	
	@Test
	fun `input directory is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				baseUrl = VALID_BASE_URL,
				inputDirectory = "",
				tenantId = VALID_TENANT_ID,
				apiKey = VALID_API_KEY,
				apiSecret = VALID_API_SECRET
			)
		}
		
		assertEquals(ConfigModel::inputDirectory.name, exception.constraintViolations.first().property)
		assertEquals("NotBlank", exception.constraintViolations.first().constraint.name)
	}
	
	@Test
	fun `output directory is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				baseUrl = VALID_BASE_URL,
				inputDirectory = VALID_INPUT_DIRECTORY,
				outputDirectory = "",
				tenantId = VALID_TENANT_ID,
				apiKey = VALID_API_KEY,
				apiSecret = VALID_API_SECRET
			)
		}
		
		assertEquals(ConfigModel::outputDirectory.name, exception.constraintViolations.first().property)
		assertEquals("NotBlank", exception.constraintViolations.first().constraint.name)
	}
	
	@Test
	fun `logging directory is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				baseUrl = VALID_BASE_URL,
				inputDirectory = VALID_INPUT_DIRECTORY,
				outputDirectory = VALID_OUTPUT_DIRECTORY,
				loggingDirectory = "",
				tenantId = VALID_TENANT_ID,
				apiKey = VALID_API_KEY,
				apiSecret = VALID_API_SECRET
			)
		}
		
		assertEquals(ConfigModel::loggingDirectory.name, exception.constraintViolations.first().property)
		assertEquals("NotBlank", exception.constraintViolations.first().constraint.name)
	}
	
	@Test
	fun `base url is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				baseUrl = "",
				tenantId = VALID_TENANT_ID,
				apiKey = VALID_API_KEY,
				apiSecret = VALID_API_SECRET
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
				baseUrl = "dummy",
				tenantId = VALID_TENANT_ID,
				apiKey = VALID_API_KEY,
				apiSecret = VALID_API_SECRET
			)
		}
		
		assertEquals(ConfigModel::baseUrl.name, exception.constraintViolations.first().property)
		assertEquals("Website", exception.constraintViolations.first().constraint.name)
	}
	
	@Test
	fun `tenant id is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				baseUrl = VALID_BASE_URL,
				tenantId = "",
				apiKey = VALID_API_KEY,
				apiSecret = VALID_API_SECRET
			)
		}
		
		assertEquals(ConfigModel::tenantId.name, exception.constraintViolations.first().property)
		assertEquals("NotBlank", exception.constraintViolations.first().constraint.name)
	}
	
	@Test
	fun `api key is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				baseUrl = VALID_BASE_URL,
				tenantId = VALID_TENANT_ID,
				apiKey = "",
				apiSecret = VALID_API_SECRET
			)
		}
		
		assertEquals(ConfigModel::apiKey.name, exception.constraintViolations.first().property)
		assertEquals("NotBlank", exception.constraintViolations.first().constraint.name)
	}
	
	@Test
	fun `api secret is blank`() {
		val exception = assertFailsWith<ConstraintViolationException> {
			ConfigModel(
				testMode = VALID_TEST_MODE,
				inputDirectory = VALID_INPUT_DIRECTORY,
				baseUrl = VALID_BASE_URL,
				tenantId = VALID_TENANT_ID,
				apiKey = VALID_API_KEY,
				apiSecret = ""
			)
		}
		
		assertEquals(ConfigModel::apiSecret.name, exception.constraintViolations.first().property)
		assertEquals("NotBlank", exception.constraintViolations.first().constraint.name)
	}
}
