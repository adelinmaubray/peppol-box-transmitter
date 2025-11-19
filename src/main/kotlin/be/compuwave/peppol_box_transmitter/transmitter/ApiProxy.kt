package be.compuwave.peppol_box_transmitter.transmitter

import be.compuwave.peppol_box_transmitter.config.AppConfig
import org.openapitools.client.apis.PeppolBoxByFlexinaAPIApi

object ApiProxy {
	val client = PeppolBoxByFlexinaAPIApi(basePath = AppConfig.config.baseUrl)
}
