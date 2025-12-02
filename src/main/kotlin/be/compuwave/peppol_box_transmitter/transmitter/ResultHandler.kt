package be.compuwave.peppol_box_transmitter.transmitter

import be.compuwave.peppol_box_transmitter.utils.printlnInGreen
import be.compuwave.peppol_box_transmitter.utils.printlnInRed
import be.compuwave.peppol_box_transmitter.utils.printlnInYellow
import org.openapitools.client.models.SendPeppolResult

object ResultHandler {
	
	/**
	 * Handles the result of a Peppol document transmission and logs the success, warning, or error messages.
	 * If errors are present in the result data, the method constructs an error message, logs it, and returns a failed `Result`.
	 * Otherwise, it logs warnings (if present) or success messages and returns the successful result.
	 *
	 * @param fileName The name of the file being sent.
	 * @param data The result of the Peppol document transmission containing details such as errors, warnings, and an ID.
	 * @return A `Result` containing the `SendPeppolResult` if the operation was successful, or a failed `Result` with an exception if errors are present.
	 */
	fun handleResultData(fileName: String, data: SendPeppolResult): Result<SendPeppolResult> {
		
		val errors = data.errors
		if (errors?.isNotEmpty() == true) {
			
			val message = errors.joinToString(" - ")
			
			printlnInRed("Document $fileName has NOT been sent:")
			printlnInYellow("\tError: $message")
			
			return Result.failure(RuntimeException(message))
		}
		
		val warnings = data.warnings
		val id = data.id
		if (warnings?.isNotEmpty() == true) {
			printlnInYellow("Document $fileName (id: $id) has been sent with warning")
			printlnInYellow("\tWarning: ${warnings.joinToString(" - ")}")
		} else {
			printlnInGreen("Document $fileName has been successfully sent with id: $id")
		}
		
		return Result.success(data)
	}
}
