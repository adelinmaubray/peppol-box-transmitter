package utils

import be.compuwave.peppol_box_transmitter.config.ConfigModel

fun provideValidConfig() = ConfigModel(
	testMode = true,
	inputDirectory = "target/test/files",
	baseUrl = "https://dummy.org",
	tenantId = "dummy",
	apiKey = "dummy",
	apiSecret = "dummy"
)
