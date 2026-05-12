package be.compuwave.peppol_box_transmitter.config

import be.compuwave.peppol_box_transmitter.utils.DATE_FORMAT
import be.compuwave.peppol_box_transmitter.utils.print
import be.compuwave.peppol_box_transmitter.utils.printWithTab
import be.compuwave.peppol_box_transmitter.utils.printlnInRed
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isLessThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isWebsite
import org.valiktor.validate
import java.io.File
import java.nio.file.Paths

data class ConfigModel(val startTime: LocalDateTime,
                       val testMode: Boolean,
                       val baseUrl: String,
                       val loggingDirectory: String,
                       val inputDirectory: String,
                       val outputDirectory: String,
                       val downloadFrom: LocalDateTime,
                       val downloadDirectory: String,
                       val tenantId: String,
                       val apiKey: String,
                       val apiSecret: String) {
	
	constructor(
		baseUrl: String,
		loggingDirectory: String? = null,
		inputDirectory: String,
		outputDirectory: String? = null,
		downloadFrom: String,
		downloadDirectory: String,
		tenantId: String,
		apiKey: String,
		apiSecret: String,
		testMode: Boolean,
	) : this(
		startTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
		testMode = testMode,
		baseUrl = baseUrl,
		loggingDirectory = loggingDirectory ?: "${Paths.get("").toAbsolutePath()}${File.separator}logs",
		inputDirectory = inputDirectory,
		outputDirectory = outputDirectory ?: "$inputDirectory${File.separator}sent",
		downloadFrom = LocalDateTime.parse(downloadFrom.ifBlank { "2026-01-01 00:00:00" }, DATE_FORMAT),
		downloadDirectory = downloadDirectory,
		tenantId = tenantId,
		apiKey = apiKey,
		apiSecret = apiSecret
	)
	
	
	init {
		try {
			
			val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
			
			validate(this) {
				validate(ConfigModel::inputDirectory).isNotBlank()
				validate(ConfigModel::outputDirectory).isNotBlank()
				validate(ConfigModel::loggingDirectory).isNotBlank()
				validate(ConfigModel::downloadFrom).isLessThan(now)
				validate(ConfigModel::downloadDirectory).isNotBlank()
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
	downloadFrom: ${downloadFrom.print()}
	downloadDirectory: $downloadDirectory
	tenantId: xxx-xxx-xx (not shown in logs)
	apiKey: xxx-xxx-xxx (not shown in logs)
	apiSecret: xxx-xxx-xxx (not shown in logs)"""
}
