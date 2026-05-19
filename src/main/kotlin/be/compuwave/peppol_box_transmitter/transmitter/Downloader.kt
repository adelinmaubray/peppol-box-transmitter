package be.compuwave.peppol_box_transmitter.transmitter

import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.utils.*
import kotlinx.datetime.LocalDateTime
import java.io.File

object Downloader {
	
	fun downloadDocuments(fromDate: LocalDateTime): Set<File> =
		ApiProxy.client.listInboundDocuments(fromDate.toOffsetDateTime())
			.asSequence()
			.mapNotNull {
				(it["id"] as? String).also { id ->
					if (id == null) {
						printlnInRed("Data from Peppol Box is not valid: documentId [${it["id"]}] is not a string. Invoice will be ignored")
					}
				}
			}
			.map { Pair(it, ApiProxy.client.downloadInboundDocumentXml(it)) }
			.map {
				
				val id = it.first
				val rawDocument = it.second
				
				val formattedXml = formatXml(rawDocument.readText())
				writeContentToFile(AppConfig.config.downloadDirectory, "$id.xml", formattedXml.toByteArray())
					.also { if (!rawDocument.delete()) printlnInYellow("Failed to delete temporary file: $rawDocument") }
				
			}
			.toSet()
			.also { println() }
}
