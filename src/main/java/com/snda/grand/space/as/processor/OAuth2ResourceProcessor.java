package com.snda.grand.space.as.processor;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.snda.grand.space.as.rest.model.Token;
import com.snda.grand.space.as.rest.model.Validation;

public interface OAuth2ResourceProcessor extends ResourceProcessor {

	Response sdoAuthorize(HttpServletRequest request)
			throws URISyntaxException, OAuthProblemException,
			OAuthSystemException, IOException;

	Token exchangeToken(HttpServletRequest request)
			throws OAuthProblemException, OAuthSystemException;

	Validation validate(String accessToken, String authorization);

}
