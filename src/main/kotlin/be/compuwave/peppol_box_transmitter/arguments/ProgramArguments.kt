package be.compuwave.peppol_box_transmitter.arguments

import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.config.ConfigModel
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
	
	private lateinit var arguments: Map<Arguments, String>
	
	private val argumentPattern = Regex("^--(.+)=(.+)$")
	
	fun getArgument(key: Arguments) = arguments[key] ?: throw NoSuchElementException("No value found for argument: $key")
	
	fun parseProgramArguments(args: Array<String>) {
		arguments = args.associate { arg ->
			val key = argumentPattern.find(arg)?.groupValues?.get(1) ?: throw IllegalArgumentException("Invalid argument: $arg")
			val value = argumentPattern.find(arg)?.groupValues?.get(2) ?: throw IllegalArgumentException("Invalid argument: $arg")
			
			Arguments.valueOf(key.uppercase()) to value
		}
		
		AppConfig.config = ConfigModel(
			testMode = getArgument(Arguments.TEST_MODE).toBoolean(),
			inputDirectory = getArgument(Arguments.INPUT_DIRECTORY),
			baseUrl = getArgument(Arguments.BASE_URL)
		)
		
		printInCyan("Program arguments parsed:")
		arguments.forEach { printWithTab("${it.key} = ${it.value}") }
		println()
	}
}
