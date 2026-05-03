package be.compuwave.peppol_box_transmitter

import be.compuwave.peppol_box_transmitter.config.ConfigModel

fun provideValidConfig() = ConfigModel(
	testMode = true,
	inputDirectory = "target/test/files",
	loggingDirectory = "target/test/files/logs",
	downloadFrom = "2026-01-01 00:00:00",
	downloadDirectory = "target/test/files/download",
	baseUrl = "https://dummy.org",
	tenantId = "dummy",
	apiKey = "dummy",
	apiSecret = "dummy"
)
