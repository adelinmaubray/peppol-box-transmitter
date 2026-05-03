package be.compuwave.peppol_box_transmitter.argument

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProgramArgumentsTest {
	
	@Test
	fun `all program arguments are valid`() {
		
		val prop = "dummy/path"
		val action = "dummy"
		
		val input = listOf(
			"--properties=$prop",
			"--action=$action"
		)
		
		ProgramArguments.parseProgramArguments(input.toTypedArray())
		
		assertEquals(prop, ProgramArguments.getPropertyFilePath())
	}
	
	@Test
	fun `properties argument is missing`() {
		val input = listOf(
			"--action=send"
		)
		
		val exception = assertFailsWith<IllegalArgumentException> {
			ProgramArguments.parseProgramArguments(input.toTypedArray())
		}
		
		assertEquals("Missing argument: --properties", exception.message)
	}
	
	@Test
	fun `action argument is missing`() {
		val input = listOf(
			"--properties=dummy/path"
		)
		
		val exception = assertFailsWith<IllegalArgumentException> {
			ProgramArguments.parseProgramArguments(input.toTypedArray())
		}
		
		assertEquals("Missing argument: --action", exception.message)
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
