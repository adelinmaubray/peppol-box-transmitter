package be.compuwave.peppol_box_transmitter.arguments

object ProgramArguments {
	
	private lateinit var arguments: Map<Arguments, String>
	
	private val argumentKeyPattern = Regex("^--(.+)=.+$")
	private val argumentValuePattern = Regex("^--.+=(.+)$")
	
	fun getArgument(key: Arguments) = arguments[key]
	
	fun parseProgramArguments(args: Array<String>) {
		arguments = args.associate { arg ->
			val key = argumentKeyPattern.find(arg)?.groupValues?.get(1) ?: throw IllegalArgumentException("Invalid argument: $arg")
			val value = argumentValuePattern.find(arg)?.groupValues?.get(1) ?: throw IllegalArgumentException("Invalid argument: $arg")
			
			Arguments.valueOf(key.uppercase()) to value
		}
		
		println("All arguments parsed:")
		arguments.forEach { println("\t${it.key} = ${it.value}") }
		println()
		
		validateAllArguments()
	}
	
	private fun validateAllArguments() {
		Arguments.entries.forEach { if (!arguments.containsKey(it)) throw IllegalArgumentException("Missing argument: $it") }
	}
}
