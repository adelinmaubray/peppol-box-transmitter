package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.arguments.ProgramArguments
import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.property.PropertyParser
import be.compuwave.peppol_box_transmitter.transmitter.Transmitter
import be.compuwave.peppol_box_transmitter.utils.getFilesInInputDirectory
import be.compuwave.peppol_box_transmitter.utils.getPropertyFile
import be.compuwave.peppol_box_transmitter.utils.moveFileToAnotherFolder

fun main(args: Array<String>) {
	
	// Parse program arguments
	ProgramArguments.parseProgramArguments(args)
	
	// Load properties from file
	PropertyParser.loadProperties(getPropertyFile(ProgramArguments.getPropertyFilePath()))
	
	// Get files in input folder
	// Send documents to Peppol network
	// And move successful files to output folder
	Transmitter.sendDocuments(getFilesInInputDirectory(AppConfig.config.inputDirectory))
		.forEach { moveFileToAnotherFolder(it, AppConfig.config.outputDirectory) }
}
