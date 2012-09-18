package com.snda.grand.space.as.processor;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.snda.grand.space.as.exception.AccountOAuthProblemException;
import com.snda.grand.space.as.rest.model.Token;
import com.snda.grand.space.as.rest.model.Validation;

public interface OAuth2ResourceProcessor extends ResourceProcessor {

	Response authorize(HttpServletRequest request)
			throws AccountOAuthProblemException, OAuthSystemException;
	
	Response sdoAuthorize(HttpServletRequest request)
			throws URISyntaxException, AccountOAuthProblemException,
			OAuthSystemException, IOException;

	Token exchangeToken(HttpServletRequest request)
			throws AccountOAuthProblemException, OAuthSystemException;

	Validation validate(String accessToken, String authorization);

}
