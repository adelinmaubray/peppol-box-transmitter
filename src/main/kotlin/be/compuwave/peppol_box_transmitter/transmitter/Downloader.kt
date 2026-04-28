package be.compuwave.peppol_box_transmitter.transmitter

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.ZoneId

object Downloader {
	
	fun downloadDocuments(fromDate: LocalDate) {
		val zoneId = ZoneId.systemDefault()
		val fromDateTime = fromDate
			.toJavaLocalDate()
			.atStartOfDay(zoneId)
			.toOffsetDateTime()
		
		val invoices = ApiProxy.client.listInboundDocuments(fromDateTime)
		
		println(invoices)
	}
}
