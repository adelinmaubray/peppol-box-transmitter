import be.compuwave.peppol_box_transmitter.utils.getFilesInInputDirectory
import be.compuwave.peppol_box_transmitter.utils.moveFileToAnotherFolder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.nio.file.NoSuchFileException
import kotlin.test.*

class FileFinderTest {
	
	lateinit var targetFolder: String
	
	@BeforeEach
	fun setUp() {
		targetFolder = "target/test"
		File(targetFolder).mkdirs()
		File(targetFolder).mkdirs()
	}
	
	@AfterEach
	fun tearDown() {
		File(targetFolder).deleteRecursively()
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
	fun `input direction does not exist`() {
		assertFailsWith<IllegalArgumentException> { getFilesInInputDirectory("does/not/exist") }
	}
	
	@Test
	fun `move a file to another folder`() {
		
		// arrange
		val originalFile = File("src/test/resources/files/file1.xml")
		
		val file = originalFile.copyTo(File(targetFolder, originalFile.name), overwrite = true)
		val originalContent = file.readText()
		val newFolder = "target/test/moved"
		
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
		assertFailsWith<NoSuchFileException> { moveFileToAnotherFolder(File("does/not/exist"), "target/test") }
	}
}
