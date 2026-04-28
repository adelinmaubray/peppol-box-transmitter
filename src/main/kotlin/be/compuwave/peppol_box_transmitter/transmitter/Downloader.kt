package be.compuwave.peppol_box_transmitter.transmitter

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneId

object Downloader {
	
	fun downloadDocuments(fromDate: LocalDateTime) {
		val fromDateTime = fromDate
			.toJavaLocalDateTime()
			.atZone(ZoneId.systemDefault())
			.toOffsetDateTime()
		
		val invoices = ApiProxy.client.listInboundDocuments(fromDateTime)
		
		println(invoices)
	}
}
