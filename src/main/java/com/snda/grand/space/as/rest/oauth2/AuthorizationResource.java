package com.snda.grand.space.as.rest.oauth2;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public interface AuthorizationResource {

	public Response authorize(@Context HttpServletRequest request)
			throws URISyntaxException, OAuthSystemException;

}
