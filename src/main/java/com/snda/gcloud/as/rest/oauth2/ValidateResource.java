package com.snda.gcloud.as.rest.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;


public interface ValidateResource {

	Response validate(HttpServletRequest request) throws OAuthSystemException;
	
}
