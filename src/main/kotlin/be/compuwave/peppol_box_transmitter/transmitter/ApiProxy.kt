package be.compuwave.peppol_box_transmitter.transmitter

import be.compuwave.peppol_box_transmitter.config.AppConfig
import org.openapitools.client.apis.PeppolBoxByFlexinaAPIApi
import org.openapitools.client.infrastructure.ApiClient

object ApiProxy {
	
	val client: PeppolBoxByFlexinaAPIApi by lazy {
		
		val api = PeppolBoxByFlexinaAPIApi(basePath = AppConfig.config.baseUrl)
		
		ApiClient.apiKey["Flx-Box-Api-Key"] = AppConfig.config.apiKey
		ApiClient.apiKey["Flx-Box-Api-Secret"] = AppConfig.config.apiSecret
		ApiClient.apiKey["Flx-Box-Api-Tenant-Id"] = AppConfig.config.tenantId

//		api.setApiKey("ApiKeyAuth", AppConfig.config.apiKey)
//		api.setApiKey("ApiSecretAuth", AppConfig.config.apiSecret)
//		api.setApiKey("TenantIdAuth", AppConfig.config.tenantId)
		
		api
	}
}
