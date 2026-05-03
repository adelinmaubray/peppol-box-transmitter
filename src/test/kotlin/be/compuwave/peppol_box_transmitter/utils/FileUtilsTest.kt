package be.compuwave.peppol_box_transmitter.utils

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.File
import kotlin.test.*

class FileUtilsTest {
	
	companion object {
		val targetFolder = File("target/test")
	}
	
	@BeforeEach
	fun setUp() {
		targetFolder.mkdirs()
	}
	
	@AfterEach
	fun tearDown() {
		targetFolder.deleteRecursively()
	}
	
	@Test
	fun `the property file is valid`() {
		assertDoesNotThrow { getPropertyFile("src/test/resources/properties/valid.properties") }
	}
	
	@Test
	fun `the provided file is not a 'properties'`() {
		assertFailsWith<IllegalArgumentException> { getPropertyFile("src/test/resources/files/file1.xml") }
	}
	
	@Test
	fun `propertyFile does not exist`() {
		assertFailsWith<NoSuchFileException> { getPropertyFile("does/not/exist.properties") }
	}
	
	@Test
	fun `find files`() {
		
		// arrange
		val path = "src/test/resources/files"
		
		// act
		val res = getFilesInInputDirectory(path)
		
		// assert
		assertEquals(3, res.size)
		res.forEach {
			assertEquals("xml", it.extension)
			assertTrue(it.exists())
		}
	}
	
	@Test
	fun `input directory is not a directory`() {
		assertFailsWith<IllegalArgumentException> { getFilesInInputDirectory("src/test/resources/files/file1.xml") }
	}
	
	@Test
	fun `input direction does not exist`() {
		assertFailsWith<IllegalArgumentException> { getFilesInInputDirectory("does/not/exist") }
	}
	
	@Test
	fun `move a file to another folder`() {
		
		// arrange
		val originalFile = File("src/test/resources/files/file1.xml")
		
		val file = originalFile.copyTo(File(targetFolder, originalFile.name), overwrite = true)
		val originalContent = file.readText()
		val newFolder = "target${File.separator}test${File.separator}moved"
		
		// act
		val res = moveFileToAnotherFolder(file, newFolder)
		
		// assert
		assertTrue(res.exists())
		assertFalse(file.exists())
		assertEquals(originalContent, res.readText())
		assertEquals(newFolder, res.parent)
		assertEquals(file.name, res.name)
	}
	
	@Test
	fun `move a file that does not exist`() {
		assertFailsWith<java.nio.file.NoSuchFileException> { moveFileToAnotherFolder(File("does/not/exist"), "target/test") }
	}

	@Test
	fun `save byte array to file`() {
		// arrange
		val directoryPath = "target/test/save"
		val fileName = "testFile.txt"
		val content = "Hello World".toByteArray()

		// act
		writeContentToFile(directoryPath, fileName, content)

		// assert
		val savedFile = File(directoryPath, fileName)
		assertTrue(savedFile.exists())
		assertContentEquals(content, savedFile.readBytes())
	}

	@Test
	fun `save byte array to file with invalid directory path`() {
		// arrange
		val directoryPath = "target/test/invalid-dir"
		val fileName = "testFile.txt"
		val content = "Hello World".toByteArray()
		
		// create a file where the directory should be
		File(directoryPath).createNewFile()

		// act & assert
		assertFails {
			writeContentToFile(directoryPath, fileName, content)
		}
	}
}
