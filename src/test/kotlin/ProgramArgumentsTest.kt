import be.compuwave.peppol_box_transmitter.arguments.ProgramArguments
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProgramArgumentsTest {
	
	@Test
	fun `all program arguments are valid`() {
		
		val prop = "dummy/path"
		
		val input = listOf(
			"--properties=$prop",
		)
		
		ProgramArguments.parseProgramArguments(input.toTypedArray())
		
		assertEquals(prop, ProgramArguments.getPropertyFilePath())
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
	fun `value is missing`() {
		val input = listOf(
			"--properties= "
		)
		
		assertFailsWith<IllegalArgumentException> {
			ProgramArguments.parseProgramArguments(input.toTypedArray())
		}
	}
}
