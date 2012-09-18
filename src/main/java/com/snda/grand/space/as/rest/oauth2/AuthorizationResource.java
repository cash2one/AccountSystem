package com.snda.grand.space.as.rest.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.snda.grand.space.as.exception.AccountOAuthProblemException;

public interface AuthorizationResource {

	Response authorize(HttpServletRequest request)
			throws AccountOAuthProblemException, OAuthSystemException;

}
