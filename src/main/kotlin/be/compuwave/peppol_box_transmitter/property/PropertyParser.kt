package be.compuwave.peppol_box_transmitter.property

import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.config.ConfigModel
import be.compuwave.peppol_box_transmitter.utils.print
import be.compuwave.peppol_box_transmitter.utils.printInCyan
import kotlinx.datetime.LocalDateTime
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

/**
 * Object responsible for parsing and loading properties from a file into the application configuration.
 *
 * This object reads properties from a specified file (supposed to be a .properties file)
 * and uses them to populate an instance of `ConfigModel`, which is assigned to the `AppConfig.config` property.
 * The loaded properties include values such as test mode, input directory, output directory, and base URL.
 *
 * Reads property values using the `ApplicationProperty` enum as keys. The expected format of the properties file
 * should adhere to these keys for successful parsing.
 */
object PropertyParser {
	
	private fun Properties.getProperty(propertyName: ApplicationProperty): String = this.getProperty(propertyName.name, "")
	private fun Properties.getPropertyOrNull(propertyName: ApplicationProperty): String? = this.getProperty(propertyName.name, null)
	
	fun loadProperties(propertiesFile: File) {
		
		val properties = Properties()
		properties.load(FileInputStream(propertiesFile))
		
		AppConfig.config = ConfigModel(
			testMode = properties.getProperty(ApplicationProperty.TEST_MODE).toBoolean(),
			inputDirectory = properties.getProperty(ApplicationProperty.INPUT_DIRECTORY),
			outputDirectory = properties.getPropertyOrNull(ApplicationProperty.OUTPUT_DIRECTORY),
			loggingDirectory = properties.getPropertyOrNull(ApplicationProperty.LOGGING_DIRECTORY),
			downloadFrom = properties.getProperty(ApplicationProperty.DOWNLOAD_FROM),
			downloadDirectory = properties.getProperty(ApplicationProperty.DOWNLOAD_DIRECTORY),
			baseUrl = properties.getProperty(ApplicationProperty.BASE_URL),
			tenantId = properties.getProperty(ApplicationProperty.TENANT_ID),
			apiKey = properties.getProperty(ApplicationProperty.API_KEY),
			apiSecret = properties.getProperty(ApplicationProperty.API_SECRET)
		)
		
		printInCyan("Properties successfully loaded:")
		println(AppConfig.config.toString())
		println()
	}
	
	fun overrideDownloadFrom(propertiesFile: File, dateTime: LocalDateTime) {
		
		val formattedDate = dateTime.print()
		
		val properties = Properties()
		properties.load(FileInputStream(propertiesFile))
		properties.setProperty(ApplicationProperty.DOWNLOAD_FROM.name, formattedDate)
		properties.store(FileOutputStream(propertiesFile), null)
		
		// replace `\:`, written by .store() method to `:`
		val unescaped = propertiesFile.readText().replace("""\:""", ":")
		propertiesFile.writeText(unescaped)
		
		printInCyan("Updating DOWNLOAD_FROM property to: $formattedDate")
	}
}
