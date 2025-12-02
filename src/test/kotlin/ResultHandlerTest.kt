import be.compuwave.peppol_box_transmitter.transmitter.ResultHandler
import org.openapitools.client.models.SendPeppolResult
import kotlin.test.Test
import kotlin.test.assertTrue

class ResultHandlerTest {
	
	@Test
	fun `handle result with errors`() {
		
		// arrange
		val data = SendPeppolResult(
			errors = listOf("error occurred"),
			warnings = listOf("warning occurred")
		)
		
		// act
		val res = ResultHandler.handleResultData("dummy", data)
		
		// assert
		assertTrue(res.isFailure)
	}
	
	@Test
	fun `handle result with warnings`() {
		
		// arrange
		val data = SendPeppolResult(
			id = "dummy",
			warnings = listOf("warning occurred")
		)
		
		// act
		val res = ResultHandler.handleResultData("dummy", data)
		
		// assert
		assertTrue(res.isSuccess)
	}
	
	@Test
	fun `handle result without errors and warnings`() {
		
		// arrange
		val data = SendPeppolResult(
			id = "dummy"
		)
		
		// act
		val res = ResultHandler.handleResultData("dummy", data)
		
		// assert
		assertTrue(res.isSuccess)
	}
}
