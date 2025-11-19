package be.compuwave.peppol_box_transmitter.utils

import java.io.File
import java.nio.file.Files

fun getFilesInInputDirectory(directoryPath: String): Set<File> {
	
	val inputDirectory = File(directoryPath)
	if (!inputDirectory.exists()) throw IllegalArgumentException("Input directory does not exist: $directoryPath")
	
	return inputDirectory.walk()
		.filter { it.isFile }
		.filter { it.extension == "xml" }
		.sortedBy { it.name }
		.toSet()
		.also { allFiles ->
			printInCyan("Files found in $directoryPath:")
			allFiles.forEach { printWithTab("${it.name}") }
			println()
		}
}

fun moveFileToAnotherFolder(fileToMove: File, newDirectory: String): File {
	
	val destinationDirectory = File(newDirectory)
	destinationDirectory.mkdirs()
	
	val newFile = File(destinationDirectory, fileToMove.name)
	
	try {
		Files.move(fileToMove.toPath(), newFile.toPath())
		return newFile
	} catch (e: Exception) {
		printlnInRed("Error moving file ${fileToMove.name} to $newDirectory")
		throw e
	}
}
