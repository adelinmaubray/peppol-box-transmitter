import be.compuwave.peppol_box_transmitter.file.getFilesInInputDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FileFinderTest {
	
	@Test
	fun `find files`() {
		
		val path = "src/test/resources/files"
		
		val res = getFilesInInputDirectory(path)
		
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
}
