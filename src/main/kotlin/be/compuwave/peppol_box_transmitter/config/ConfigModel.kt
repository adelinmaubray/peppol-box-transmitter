package be.compuwave.peppol_box_transmitter.config

import be.compuwave.peppol_box_transmitter.utils.printWithTab
import be.compuwave.peppol_box_transmitter.utils.printlnInRed
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isWebsite
import org.valiktor.validate
import java.nio.file.Paths

data class ConfigModel(val testMode: Boolean,
					   val baseUrl: String,
					   val loggingDirectory: String,
					   val inputDirectory: String,
					   val outputDirectory: String,
					   val tenantId: String,
					   val apiKey: String,
					   val apiSecret: String) {
	
	constructor(
		baseUrl: String,
		loggingDirectory: String? = null,
		inputDirectory: String,
		outputDirectory: String? = null,
		tenantId: String,
		apiKey: String,
		apiSecret: String,
		testMode: Boolean,
	) : this(
		testMode,
		baseUrl,
		loggingDirectory ?: "${Paths.get("").toAbsolutePath()}/logs",
		inputDirectory,
		outputDirectory ?: "$inputDirectory/sent",
		tenantId,
		apiKey,
		apiSecret
	)
	
	
	init {
		try {
			validate(this) {
				validate(ConfigModel::inputDirectory).isNotBlank()
				validate(ConfigModel::outputDirectory).isNotBlank()
				validate(ConfigModel::loggingDirectory).isNotBlank()
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
	loggingDirectory: $loggingDirectory
	inputDirectory: $inputDirectory
	outputDirectory: $outputDirectory
	tenantId: xxx-xxx-xx (not shown in logs)
	apiKey: xxx-xxx-xxx (not shown in logs)
	apiSecret: xxx-xxx-xxx (not shown in logs)"""
}
