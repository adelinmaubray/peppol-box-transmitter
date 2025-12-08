package be.compuwave.peppol_box_transmitter.utils

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger {
	
	private val logBuffer = ByteArrayOutputStream()
	private val originalOut = System.out
	private val originalErr = System.err
	
	// Create streams that write to both console and buffer
	private val teeOut: PrintStream = PrintStream(TeeOutputStream(originalOut, logBuffer))
	private val teeErr: PrintStream = PrintStream(TeeOutputStream(originalErr, logBuffer))
	
	init {
		
		// Redirect system streams
		System.setOut(teeOut)
		System.setErr(teeErr)
	}
	
	fun writeLogsToFile(directoryPath: String) {
		try {
			
			// create log directory
			val logDirectory = File(directoryPath)
			logDirectory.mkdirs()
			
			// create log file
			val format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
			val logFile = File(directoryPath, "${format.format(LocalDateTime.now())}.log")
			logFile.createNewFile()
			
			Files.write(logFile.toPath(), logBuffer.toByteArray())
			
			println()
			printInCyan("Logs written to $directoryPath")
			
			restore()
		} catch (e: Exception) {
			originalOut.println("Failed to write logs: ${e.message}")
		}
	}
	
	fun restore() {
		System.setOut(originalOut)
		System.setErr(originalErr)
	}
	
	// Custom OutputStream that writes to multiple destinations
	class TeeOutputStream(private vararg val outputs: OutputStream) : OutputStream() {
		override fun write(b: Int) {
			outputs.forEach { it.write(b) }
		}
		
		override fun write(b: ByteArray) {
			outputs.forEach { it.write(b) }
		}
		
		override fun write(b: ByteArray, off: Int, len: Int) {
			outputs.forEach { it.write(b, off, len) }
		}
		
		override fun flush() {
			outputs.forEach { it.flush() }
		}
		
		override fun close() {
			outputs.forEach { it.close() }
		}
	}
}

fun printlnInRed(message: String?) = println("\u001B[31m$message\u001B[0m")
fun printlnInGreen(message: String?) = println("\u001B[32m$message\u001B[0m")
fun printlnInYellow(message: String?) = println("\u001B[33m$message\u001B[0m")
fun printInCyan(message: String?) = println("\u001B[36m$message\u001B[0m")

fun printWithTab(message: String?) = println("\t$message")
