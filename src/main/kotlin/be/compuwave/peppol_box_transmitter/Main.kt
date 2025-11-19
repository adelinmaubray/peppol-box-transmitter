package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.arguments.Arguments
import be.compuwave.peppol_box_transmitter.arguments.ProgramArguments
import be.compuwave.peppol_box_transmitter.transmitter.Transmitter
import be.compuwave.peppol_box_transmitter.utils.getFilesInInputDirectory

fun main(args: Array<String>) {
	
	// Parse and validate program arguments
	ProgramArguments.parseProgramArguments(args)
	
	// Get files in input folder
	val xmlFilesToHandle = getFilesInInputDirectory(ProgramArguments.getArgument(Arguments.INPUT_DIRECTORY))
	
	// Send document to Peppol network
	val xmlFileTreated = Transmitter.sendDocuments(xmlFilesToHandle)
}
