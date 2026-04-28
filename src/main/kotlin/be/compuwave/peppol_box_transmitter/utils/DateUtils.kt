package be.compuwave.peppol_box_transmitter.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

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
