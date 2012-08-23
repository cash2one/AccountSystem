package com.snda.gcloud.as.rest.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public interface TokenResource {

	Response exchangeToken(HttpServletRequest request) throws OAuthSystemException;
	
	Response exchangeTokenGet(HttpServletRequest request) throws OAuthSystemException;
	
}
