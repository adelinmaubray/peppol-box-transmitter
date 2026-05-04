package be.compuwave.peppol_box_transmitter.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toJavaLocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

val DATE_FORMAT = LocalDateTime.Format {
	year()
	char('-')
	monthNumber()
	char('-')
	dayOfMonth()
	char(' ')
	hour()
	char(':')
	minute()
	char(':')
	second()
}

fun LocalDateTime.print() = this.format(DATE_FORMAT)

fun LocalDateTime.toOffsetDateTime(): OffsetDateTime = this
	.toJavaLocalDateTime()
	.atZone(ZoneId.systemDefault())
	.toOffsetDateTime()
