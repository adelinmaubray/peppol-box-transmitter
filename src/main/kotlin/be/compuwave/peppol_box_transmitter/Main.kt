package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.argument.ProgramArguments
import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.property.PropertyParser
import be.compuwave.peppol_box_transmitter.transmitter.Downloader
import be.compuwave.peppol_box_transmitter.transmitter.Sender
import be.compuwave.peppol_box_transmitter.utils.*

fun main(args: Array<String>) {
	
	val logger = Logger()
	
	try {
		
		// Parse program arguments
		ProgramArguments.parseProgramArguments(args)
		
		// Load properties from file
		val propertiesFile = getPropertyFile(ProgramArguments.getPropertyFilePath())
		PropertyParser.loadProperties(propertiesFile)
		
		// TODO move to a new orchestrator?
		when (ProgramArguments.getAction()) {
			ProgramArguments.ProgramAction.SEND ->
				// Get files in input folder
				// Send documents to Peppol network
				// And move successful files to output folder
				Sender.sendDocuments(getFilesInInputDirectory(AppConfig.config.inputDirectory))
					.also { println() }
					.forEach { moveFileToAnotherFolder(it, AppConfig.config.outputDirectory) }
			
			ProgramArguments.ProgramAction.DOWNLOAD ->
				// Download documents from Peppol network
				// And update DOWNLOAD_FROM property
				Downloader.downloadDocuments(AppConfig.config.downloadFrom)
					.also { PropertyParser.overrideDownloadFrom(propertiesFile, AppConfig.config.startTime) }
		}
		
		
		// Export logs to file
		logger.writeLogsToFile(AppConfig.config.loggingDirectory)
	} catch (exception: Exception) {
		
		exception.message?.let { printlnInRed(exception.message) }
		println()
		throw exception
	}
}
