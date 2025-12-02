package be.compuwave.peppol_box_transmitter.transmitter

import be.compuwave.peppol_box_transmitter.config.AppConfig
import org.openapitools.client.models.SendPeppolResult
import java.io.File

object Transmitter {
	
	/**
	 * Sends a set of XML documents to a given endpoint and logs the result of the operation.
	 * Successfully sent documents are returned.
	 *
	 * @param xmlDocuments A set of XML files to be sent.
	 * @return A set of XML files that were successfully sent.
	 */
	fun sendDocuments(xmlDocuments: Set<File>): Set<File> = xmlDocuments
		.asSequence()
		.map { it to sendDocument(it) }
		.filter { it.second.isSuccess }
		.map { it.first }
		.toSet()
	
	/**
	 * Sends a single XML document via the Peppol network using the client API.
	 * The method reads the content of the provided XML document file and
	 * attempts to send it through the `sendPeppolDocument` API.
	 * It returns a `Result` containing the outcome of the send operation.
	 *
	 * @param xmlDocument The XML document file to be sent. This file must contain
	 * a valid UBL XML invoice or credit note conforming to the Peppol BIS Billing 3.0 specification.
	 * @return A `Result` object containing a `SendPeppolResult` on successful execution
	 * or an exception on failure.
	 */
	fun sendDocument(xmlDocument: File): Result<SendPeppolResult> =
		ResultHandler.handleResultData(
			xmlDocument.name,
			ApiProxy.client.sendPeppolDocument(
				xmlDocument.readText(),
				AppConfig.config.testMode
			)
		)
	
}
