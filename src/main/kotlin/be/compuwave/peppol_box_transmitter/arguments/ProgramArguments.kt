package be.compuwave.peppol_box_transmitter.arguments

import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.config.ConfigModel

object ProgramArguments {
	
	private lateinit var arguments: Map<Arguments, String>
	
	private val argumentKeyPattern = Regex("^--(.+)=.+$")
	private val argumentValuePattern = Regex("^--.+=(.+)$")
	
	fun getArgument(key: Arguments) = arguments[key] ?: throw NoSuchElementException("No value found for argument: $key")
	
	fun parseProgramArguments(args: Array<String>) {
		arguments = args.associate { arg ->
			val key = argumentKeyPattern.find(arg)?.groupValues?.get(1) ?: throw IllegalArgumentException("Invalid argument: $arg")
			val value = argumentValuePattern.find(arg)?.groupValues?.get(1) ?: throw IllegalArgumentException("Invalid argument: $arg")
			
			Arguments.valueOf(key.uppercase()) to value
		}
		
		AppConfig.config = ConfigModel(
			testMode = getArgument(Arguments.TEST_MODE).toBoolean(),
			inputDirectory = getArgument(Arguments.INPUT_DIRECTORY),
			baseUrl = getArgument(Arguments.BASE_URL)
		)
		
		println("Program arguments parsed:")
		arguments.forEach { println("\t${it.key} = ${it.value}") }
		println()
	}
}
