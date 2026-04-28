package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.arguments.ProgramArguments
import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.property.PropertyParser
import be.compuwave.peppol_box_transmitter.transmitter.Downloader
import be.compuwave.peppol_box_transmitter.transmitter.Sender
import be.compuwave.peppol_box_transmitter.utils.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

fun main(args: Array<String>) {
	
	// TODO retrieve the date from .properties file or program args
	val date = LocalDate(2026, Month.MARCH, 1)
	
	val logger = Logger()
	
	try {
		
		// Parse program arguments
		ProgramArguments.parseProgramArguments(args)
		
		// Load properties from file
		PropertyParser.loadProperties(getPropertyFile(ProgramArguments.getPropertyFilePath()))
		
		// TODO move to a new orchestrator?
		when (ProgramArguments.getAction()) {
			ProgramArguments.ProgramAction.SEND ->
				// Get files in input folder
				// Send documents to Peppol network
				// And move successful files to output folder
				Sender.sendDocuments(getFilesInInputDirectory(AppConfig.config.inputDirectory))
					.also { println() }
					.forEach { moveFileToAnotherFolder(it, AppConfig.config.outputDirectory) }
			
			ProgramArguments.ProgramAction.DOWNLOAD -> Downloader.downloadDocuments(date)
		}
		
		
		// Export logs to file
		logger.writeLogsToFile(AppConfig.config.loggingDirectory)
	} catch (exception: Exception) {
		
		exception.message?.let { printlnInRed(exception.message) }
		println()
		throw exception
	}
}
