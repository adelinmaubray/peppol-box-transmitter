package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.arguments.ProgramArguments
import org.openapitools.client.apis.PeppolBoxByFlexinaAPIApi

fun main(args: Array<String>) {
	
	// Parse and validate program arguments
	ProgramArguments.parseProgramArguments(args)
	
	// Get file in folder
	
	
	val api = PeppolBoxByFlexinaAPIApi(basePath = "")
}
