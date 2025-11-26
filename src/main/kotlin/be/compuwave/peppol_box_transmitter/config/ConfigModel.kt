package be.compuwave.peppol_box_transmitter.config

import be.compuwave.peppol_box_transmitter.utils.printWithTab
import be.compuwave.peppol_box_transmitter.utils.printlnInRed
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isWebsite
import org.valiktor.validate

data class ConfigModel(val testMode: Boolean,
					   val baseUrl: String,
					   val inputDirectory: String,
					   val outputDirectory: String = "$inputDirectory/sent",
					   val tenantId: String,
					   val apiKey: String,
					   val apiSecret: String) {
	init {
		try {
			validate(this) {
				validate(ConfigModel::inputDirectory).isNotBlank()
				validate(ConfigModel::outputDirectory).isNotBlank()
				validate(ConfigModel::baseUrl).isNotBlank().isWebsite()
				validate(ConfigModel::tenantId).isNotBlank()
				validate(ConfigModel::apiKey).isNotBlank()
				validate(ConfigModel::apiSecret).isNotBlank()
			}
		} catch (exception: ConstraintViolationException) {
			throw exception.also {
				printlnInRed("Some arguments are not valid:")
				exception.constraintViolations
					.map { "${it.property}: ${it.constraint.name}" }
					.forEach(::printWithTab)
			}
		}
	}
	
	override fun toString(): String =
"""	testMode: $testMode
	baseUrl: $baseUrl
	inputDirectory: $inputDirectory
	outputDirectory: $outputDirectory
	tenantId: xxx-xxx-xx (not shown in logs)
	apiKey: xxx-xxx-xxx (not shown in logs)
	apiSecret: xxx-xxx-xxx (not shown in logs)"""
}
