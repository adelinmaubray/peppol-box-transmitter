package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.argument.ProgramArguments
import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.transmitter.Orchestrator
import be.compuwave.peppol_box_transmitter.utils.Logger
import be.compuwave.peppol_box_transmitter.utils.printlnInRed

fun main(args: Array<String>) {
	
	val logger = Logger()
	
	try {
		
		// Parse program arguments
		ProgramArguments.parseProgramArguments(args)
		
		// Execute action
		Orchestrator.execute()
		
		// Export logs to file
		logger.writeLogsToFile(AppConfig.config.loggingDirectory)
	} catch (exception: Exception) {
		
		exception.message?.let { printlnInRed(exception.message) }
		println()
		throw exception
	}
}
