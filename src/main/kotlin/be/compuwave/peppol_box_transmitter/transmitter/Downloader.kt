package be.compuwave.peppol_box_transmitter.transmitter

import be.compuwave.peppol_box_transmitter.config.AppConfig
import be.compuwave.peppol_box_transmitter.utils.printlnInRed
import be.compuwave.peppol_box_transmitter.utils.writeContentToFile
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneId

object Downloader {
	
	fun downloadDocuments(fromDate: LocalDateTime) {
		val fromDateTime = fromDate
			.toJavaLocalDateTime()
			.atZone(ZoneId.systemDefault())
			.toOffsetDateTime()
		
		val downloadLocation = AppConfig.config.downloadDirectory
		
		ApiProxy.client.listInboundDocuments(fromDateTime)
			.asSequence()
			.mapNotNull {
				(it["id"] as? String).also { id ->
					if (id == null) {
						printlnInRed("Data from Peppol Box is not valid: documentId [${it["id"]}] is not a string")
					}
				}
			}
			.map { Pair(it, ApiProxy.client.downloadInboundDocumentXml(it)) }
			.forEach {
				
				val id = it.first
				val rawDocument = it.second.readBytes()
				
				writeContentToFile(downloadLocation, "$id.xml", rawDocument)
				it.second.delete()
			}
	}
}
