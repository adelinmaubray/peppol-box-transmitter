package be.compuwave.peppol_box_transmitter.config

/**
 * Singleton object responsible for storing and providing access to application configuration settings.
 *
 * The `config` property is initialized with an instance of `ConfigModel`, which contains the parsed
 * configuration values, such as test mode, input directory, and base URL. This object serves
 * as a central point for accessing configuration values across the application.
 *
 * The `config` property must be initialized before usage, and attempting to access it without proper
 * initialization will result in an exception.
 */
object AppConfig {
	lateinit var config: ConfigModel
}
