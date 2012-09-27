package com.snda.grand.space.as.processor;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.snda.grand.space.as.exception.ASOAuthProblemException;
import com.snda.grand.space.as.rest.model.Token;
import com.snda.grand.space.as.rest.model.Validation;

public interface OAuth2ResourceProcessor extends ResourceProcessor {

	Response authorize(HttpServletRequest request)
			throws ASOAuthProblemException, OAuthSystemException;
	
	Response sdoAuthorize(HttpServletRequest request)
			throws URISyntaxException, ASOAuthProblemException,
			OAuthSystemException, IOException;

	Token exchangeToken(HttpServletRequest request)
			throws ASOAuthProblemException, OAuthSystemException;

	Validation validate(String accessToken, String authorization);

}
