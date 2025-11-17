package be.compuwave.peppol_box_transmitter.file

import java.io.File

fun getFilesInInputDirectory(path: String): Set<File> {
	
	val inputDirectory = File(path)
	if (!inputDirectory.exists()) throw IllegalArgumentException("Input directory does not exist: $path")
	
	return inputDirectory.walk()
		.filter { it.isFile }
		.filter { it.extension == "xml" }
		.toSet()
		.also { allFiles ->
			println("Files found in $path:")
			allFiles.forEach { println("\t${it.name}") }
			println()
		}
}
