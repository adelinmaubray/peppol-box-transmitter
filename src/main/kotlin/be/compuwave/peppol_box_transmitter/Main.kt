package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.arguments.Arguments
import be.compuwave.peppol_box_transmitter.arguments.ProgramArguments
import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.file.getFilesInInputDirectory
import org.openapitools.client.apis.PeppolBoxByFlexinaAPIApi

fun main(args: Array<String>) {
	
	// Parse and validate program arguments
	ProgramArguments.parseProgramArguments(args)
	
	// Get files in input folder
	val xmlFiles = getFilesInInputDirectory(ProgramArguments.getArgument(Arguments.INPUT_DIRECTORY))
	
	val api = PeppolBoxByFlexinaAPIApi(basePath = "")
	api.sendPeppolDocument("", AppConfig.config.testMode)
}
