package utils

import be.compuwave.peppol_box_transmitter.config.ConfigModel

fun provideValidConfig() = ConfigModel(
	testMode = true,
	inputDirectory = "src/test/resources/files",
	baseUrl = "https://dummy.org"
)
