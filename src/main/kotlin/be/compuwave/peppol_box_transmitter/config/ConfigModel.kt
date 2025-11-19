package be.compuwave.peppol_box_transmitter.config

import be.compuwave.peppol_box_transmitter.utils.printlnInRed
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isWebsite
import org.valiktor.validate

data class ConfigModel(val testMode: Boolean,
					   val inputDirectory: String,
					   val outputDirectory: String = "$inputDirectory/sent",
					   val baseUrl: String) {
	init {
		try {
			validate(this) {
				validate(ConfigModel::inputDirectory).isNotBlank()
				validate(ConfigModel::outputDirectory).isNotBlank()
				validate(ConfigModel::baseUrl).isNotBlank().isWebsite()
			}
		} catch (exception: ConstraintViolationException) {
			throw exception.also {
				printlnInRed("Some arguments are not valid:")
				exception.constraintViolations
					.map { "${it.property}: ${it.constraint.name}" }
					.forEach(::printlnInRed)
			}
		}
	}
}
