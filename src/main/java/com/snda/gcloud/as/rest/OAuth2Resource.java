package com.snda.gcloud.as.rest;

import javax.ws.rs.core.Response;

public interface OAuth2Resource {
	
	Response authorize(String clientId,
			String responseType,
			String redirectUri,
			String scope);
	
	Response exchangeAccessToken(String clientId,
			String clientSecret,
			String grantType,
			String redirectUri,
			String code);
	
}
