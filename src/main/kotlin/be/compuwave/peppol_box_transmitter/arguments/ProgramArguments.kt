package be.compuwave.peppol_box_transmitter.arguments

import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.config.ConfigModel
import be.compuwave.peppol_box_transmitter.property.ApplicationProperty
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
	
	private lateinit var applicationProperty: Map<ApplicationProperty, String>
	
	private val argumentPattern = Regex("^--(.+)=(.+)$")
	
	private fun getArgument(key: ApplicationProperty) = applicationProperty[key] ?: throw NoSuchElementException("No value found for argument: $key")
	
	fun parseProgramArguments(args: Array<String>) {
		applicationProperty = args.associate { arg ->
			val key = argumentPattern.find(arg)?.groupValues?.get(1) ?: throw IllegalArgumentException("Invalid argument: $arg")
			val value = argumentPattern.find(arg)?.groupValues?.get(2) ?: throw IllegalArgumentException("Invalid argument: $arg")
			
			ApplicationProperty.valueOf(key.uppercase()) to value
		}
		
		AppConfig.config = ConfigModel(
			testMode = getArgument(ApplicationProperty.TEST_MODE).toBoolean(),
			inputDirectory = getArgument(ApplicationProperty.INPUT_DIRECTORY),
			outputDirectory = getArgument(ApplicationProperty.OUTPUT_DIRECTORY),
			baseUrl = getArgument(ApplicationProperty.BASE_URL)
		)
		
		printInCyan("Program arguments parsed:")
		applicationProperty.forEach { printWithTab("${it.key} = ${it.value}") }
		println()
	}
}
