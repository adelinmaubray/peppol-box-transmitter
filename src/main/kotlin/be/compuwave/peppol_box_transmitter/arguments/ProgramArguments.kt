package be.compuwave.peppol_box_transmitter.arguments

import be.compuwave.peppol_box_transmitter.utils.printInCyan
import be.compuwave.peppol_box_transmitter.utils.printWithTab

/**
 * Provides methods to parse and handle program arguments passed to the application.
 * Expects arguments in the format `--key=value` and validates them against predefined keys.
 *
 * This object maps the arguments to the `Arguments` enum and sets up the application configuration
 * using the parsed values. If any required arguments are missing or have invalid formats,
 * an exception is thrown.
 *
 * Responsibilities:
 * - Parsing the program arguments.
 * - Validating the arguments against the `Arguments` enum.
 * - Initializing the application configuration.
 * - Logging the parsed arguments.
 */
object ProgramArguments {
	
	enum class ProgramArgument {
		PROPERTIES
	}
	
	private lateinit var parsedArguments: Map<ProgramArgument, String>
	private val argumentPattern = Regex("^--(.+)=(.+)$")
	
	private fun getArgument(key: ProgramArgument) = parsedArguments[key] ?: throw NoSuchElementException("No value found for argument: $key")
	
	fun parseProgramArguments(args: Array<String>) {
		parsedArguments = args.associate { arg ->
			val key = argumentPattern.find(arg)?.groupValues?.get(1) ?: throw IllegalArgumentException("Invalid argument: $arg")
			val value = argumentPattern.find(arg)?.groupValues?.get(2) ?: throw IllegalArgumentException("Invalid argument: $arg")
			
			ProgramArgument.valueOf(key.uppercase()) to value
		}
		
		validateArguments()
		
		printInCyan("Program arguments parsed:")
		parsedArguments.forEach { printWithTab("${it.key} = ${it.value}") }
		println()
	}
	
	private fun validateArguments() {
		// check all arguments are present
		// and are not empty
		ProgramArgument.entries.forEach {
			if (!parsedArguments.containsKey(it)) throw IllegalArgumentException("Missing argument: ${it.name}")
			if (parsedArguments[it].isNullOrBlank()) throw IllegalArgumentException("Argument ${it.name} is blank")
		}
	}
	
	fun getPropertyFilePath() = getArgument(ProgramArgument.PROPERTIES)
}
