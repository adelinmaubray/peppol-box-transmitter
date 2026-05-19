package be.compuwave.peppol_box_transmitter.transmitter

import be.compuwave.peppol_box_transmitter.argument.ProgramArguments
import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.property.PropertyParser
import be.compuwave.peppol_box_transmitter.utils.getFilesInInputDirectory
import be.compuwave.peppol_box_transmitter.utils.getPropertyFile
import be.compuwave.peppol_box_transmitter.utils.moveFileToAnotherFolder
import java.io.File

object Orchestrator {
	
	fun execute() {
		
		// Load properties from file
		val propertiesFile = getPropertyFile(ProgramArguments.getPropertyFilePath())
		PropertyParser.loadProperties(propertiesFile)
		
		when (ProgramArguments.getAction()) {
			ProgramArguments.ProgramAction.SEND -> send()
			ProgramArguments.ProgramAction.DOWNLOAD -> download(propertiesFile)
		}
	}
	
	private fun send() {
		// Get files in input folder
		// Send documents to Peppol network
		// And move successful files to output folder
		Sender.sendDocuments(getFilesInInputDirectory(AppConfig.config.inputDirectory))
			.also { println() }
			.forEach { moveFileToAnotherFolder(it, AppConfig.config.outputDirectory) }
	}
	
	private fun download(propertiesFile: File) {
		// Download documents from Peppol network
		// And update DOWNLOAD_FROM property
		Downloader.downloadDocuments(AppConfig.config.downloadFrom)
			.also { PropertyParser.overrideDownloadFrom(propertiesFile, AppConfig.config.startTime) }
	}
}
