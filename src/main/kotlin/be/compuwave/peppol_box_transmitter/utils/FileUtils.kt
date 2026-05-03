package be.compuwave.peppol_box_transmitter.utils

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Retrieves a property file from the specified file path.
 * Ensures that the file exists and has the '.properties' extension.
 *
 * @param propertyFilePath The path to the property file.
 * @return The property file as a [File] object.
 * @throws IllegalArgumentException If the file does not have the '.properties' extension.
 * @throws NoSuchFileException If the file does not exist at the specified path.
 */
fun getPropertyFile(propertyFilePath: String): File {
	val propertyFile = File(propertyFilePath)
	
	require(propertyFile.extension == "properties") { "The provided property file does not have the '.properties' extension: ${propertyFile.name}'" }
	
	return if (propertyFile.exists()) propertyFile else throw NoSuchFileException(
		file = propertyFile,
		reason = "Property file does not exist: $propertyFilePath"
	)
}

/**
 * Retrieves all XML files in the specified input directory, sorted by their file names.
 *
 * @param directoryPath The path to the directory from which files will be retrieved.
 * The directory must exist and must be a valid directory.
 * @return A set of XML files found in the provided directory. If no files are found, the returned set will be empty.
 * An error will be thrown if the provided path is invalid or the directory does not exist.
 */
fun getFilesInInputDirectory(directoryPath: String): Set<File> {
	
	val inputDirectory = File(directoryPath)
	require(inputDirectory.isDirectory()) { "The provided path is not a directory: $directoryPath" }
	require(inputDirectory.exists()) { "Input directory does not exist: ${inputDirectory.absolutePath}" }
	
	return inputDirectory.listFiles()!!
		.filter { it.isFile }
		.filter { it.extension == "xml" }
		.sortedBy { it.name }
		.toSet()
		.also { allFiles ->
			printInCyan("Files found in $directoryPath:")
			if (allFiles.isNotEmpty()) {
				allFiles.forEach { printWithTab("${it.name}") }
				println()
			} else {
				printlnInRed("\tNo files found in $directoryPath")
			}
		}
}

/**
 * Moves a file from its current location to a specified directory.
 * The method creates the target directory if it does not exist and handles potential exceptions
 * during the file moving process.
 *
 * @param fileToMove the file to be moved
 * @param newDirectory the path of the directory to move the file into
 * @return the new file object representing the moved file in the target directory
 * @throws Exception if an error occurs during the file moving process
 */
fun moveFileToAnotherFolder(fileToMove: File, newDirectory: String): File {
	
	val destinationDirectory = File(newDirectory)
	destinationDirectory.mkdirs()
	
	val newFile = File(destinationDirectory, fileToMove.name)
	
	try {
		Files.move(fileToMove.toPath(), newFile.toPath())
		printInCyan("File ${fileToMove.name} moved to ${destinationDirectory.absolutePath}")
		return newFile
	} catch (e: Exception) {
		printlnInRed("Error moving file ${fileToMove.name} to ${destinationDirectory.absolutePath}")
		throw e
	}
}

/**
 * Saves a ByteArray to a file in the specified directory.
 * The method creates the target directory if it does not exist.
 *
 * @param directoryPath the path of the directory to save the file into
 * @param fileName the name of the file to be saved
 * @param content the content of the file as a ByteArray
 */
fun writeContentToFile(directoryPath: String, fileName: String, content: ByteArray) {
	
	val directory = File(directoryPath)
	if (!directory.exists()) {
		directory.mkdirs()
	}

	val filePath = Paths.get(directoryPath, fileName)
	Files.write(filePath, content)
	printlnInGreen("Document saved to $filePath")
}
