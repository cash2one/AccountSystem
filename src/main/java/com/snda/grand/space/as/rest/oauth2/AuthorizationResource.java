package com.snda.grand.space.as.rest.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public interface AuthorizationResource {

	public Response authorize(HttpServletRequest request)
			throws OAuthProblemException, OAuthSystemException;

}
