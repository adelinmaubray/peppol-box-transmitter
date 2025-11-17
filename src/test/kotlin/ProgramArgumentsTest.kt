import be.compuwave.peppol_box_transmitter.arguments.ProgramArguments
import be.compuwave.peppol_box_transmitter.config.AppConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ProgramArgumentsTest {
	
	@Test
	fun `all program arguments are valid`() {
		
		val inputDir = "src/main/resources/input"
		val baseUrl = "https://www.jetbrains.com"
		
		val input = listOf(
			"--test_mode=true",
			"--input_directory=$inputDir",
			"--base_url=$baseUrl"
		)
		
		ProgramArguments.parseProgramArguments(input.toTypedArray())
		
		assertTrue(AppConfig.config.testMode)
		assertEquals(AppConfig.config.inputDirectory, inputDir)
		assertEquals(AppConfig.config.baseUrl, baseUrl)
	}
	
	@Test
	fun `key value is malformed`() {
		val input = listOf(
			"--test="
		)
		
		assertFailsWith<IllegalArgumentException> {
			ProgramArguments.parseProgramArguments(input.toTypedArray())
		}
	}
	
	@Test
	fun `one argument is missing`() {
		
		val input = listOf(
			"--test_mode=true",
			"--input_directory=src/main/resources/input"
		)
		
		assertFailsWith<NoSuchElementException> { ProgramArguments.parseProgramArguments(input.toTypedArray()) }
	}
}
