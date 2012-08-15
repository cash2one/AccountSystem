package com.snda.gcloud.as.rest;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public interface OAuth2Resource {
	
	Response exchangeAccessToken(HttpServletRequest request) 
			throws OAuthSystemException;
	
	Response authorize(HttpServletRequest request)
	        throws URISyntaxException, OAuthSystemException;
}
