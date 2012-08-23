package com.snda.gcloud.as.rest.oauth2;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public interface AuthorizationResource {

	Response authorize(String clientId, 
			String responseType,
			String redirectUri,
			String scope) throws URISyntaxException;

	Response authorizeGet(HttpServletRequest request)
			throws URISyntaxException, OAuthSystemException;

}
