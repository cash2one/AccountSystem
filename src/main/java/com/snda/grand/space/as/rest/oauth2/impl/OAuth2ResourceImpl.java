package com.snda.grand.space.as.rest.oauth2.impl;

import static com.snda.grand.space.as.rest.util.Preconditions.checkScope;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.snda.grand.space.as.exception.AccountOAuthProblemException;
import com.snda.grand.space.as.processor.OAuth2ResourceProcessor;
import com.snda.grand.space.as.rest.model.Token;
import com.snda.grand.space.as.rest.model.Validation;
import com.snda.grand.space.as.rest.oauth2.AuthorizationResource;
import com.snda.grand.space.as.rest.oauth2.SdoAuthResource;
import com.snda.grand.space.as.rest.oauth2.TokenResource;
import com.snda.grand.space.as.rest.oauth2.ValidateResource;


@Service
@Path("oauth2")
public class OAuth2ResourceImpl implements AuthorizationResource,
		TokenResource, ValidateResource, SdoAuthResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceImpl.class);
	
	private final OAuth2ResourceProcessor oauth2ResourceProcessor;
	
	public OAuth2ResourceImpl(OAuth2ResourceProcessor oauth2ResourceProcessor) {
		this.oauth2ResourceProcessor = oauth2ResourceProcessor;
		LOGGER.info("OAuth2ResourceImpl initialized.");
	}

	@Override
	@POST
	@Path("token")
	public Token exchangeToken(@Context HttpServletRequest request)
			throws AccountOAuthProblemException, OAuthSystemException {
		return oauth2ResourceProcessor.exchangeToken(request);
	}

	@Override
	@GET
	@Path("authorize")
	public Response authorize(@Context HttpServletRequest request)
			throws AccountOAuthProblemException, OAuthSystemException {
		String scope = request.getParameter("scope");
		if (scope != null) {
			checkScope(scope);
		}
		return oauth2ResourceProcessor.authorize(request);
	}
	
	@Override
	@GET
	@Path("sdo_auth")
	public Response sdoAuthorize(@Context HttpServletRequest request)
			throws URISyntaxException, AccountOAuthProblemException,
			OAuthSystemException, IOException {
		return oauth2ResourceProcessor.sdoAuthorize(request);
	}
	
	@Override
	@POST
	@Path("validate")
	public Validation validate(@Context HttpServletRequest request) {
		String accessToken = request.getParameter("access_token");
		String authorization = request.getParameter("authorization");
		return oauth2ResourceProcessor.validate(accessToken, authorization);
	}

}
