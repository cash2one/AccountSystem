package com.snda.grand.space.as.rest.oauth2.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.snda.grand.space.as.processor.OAuth2ResourceProcessor;
import com.snda.grand.space.as.rest.model.Token;
import com.snda.grand.space.as.rest.model.Validation;
import com.snda.grand.space.as.rest.oauth2.AuthorizationResource;
import com.snda.grand.space.as.rest.oauth2.SdoAuthResource;
import com.snda.grand.space.as.rest.oauth2.TokenResource;
import com.snda.grand.space.as.rest.oauth2.ValidateResource;
import com.snda.grand.space.as.rest.util.Constants;


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
			throws OAuthProblemException, OAuthSystemException {
		return oauth2ResourceProcessor.exchangeToken(request);
	}

	@Override
	@GET
	@Path("authorize")
	public Response authorize(@Context HttpServletRequest request)
			throws OAuthProblemException, OAuthSystemException {
		OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

		// build response according to response_type
		String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
		LOGGER.info("Response type:<{}>", responseType);

		OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
				.authorizationResponse(request,
						HttpServletResponse.SC_FOUND);

		String redirectURI = oauthRequest
				.getParam(OAuth.OAUTH_REDIRECT_URI);
		LOGGER.info("Redirect URI:<{}>", redirectURI);
		
		HashMap<String, String> queries = Maps.newHashMap();
		if (oauthRequest.getClientId() != null
				&& !oauthRequest.getClientId().equalsIgnoreCase("")) {
			queries.put(OAuth.OAUTH_CLIENT_ID, oauthRequest.getClientId());
		}
		if (responseType != null && !responseType.equalsIgnoreCase("")) {
			queries.put(OAuth.OAUTH_RESPONSE_TYPE, responseType);
		}
		if (oauthRequest.getRedirectURI() != null
				&& !oauthRequest.getRedirectURI().equalsIgnoreCase("")) {
			queries.put(OAuth.OAUTH_REDIRECT_URI,
					oauthRequest.getRedirectURI());
		}
		if (oauthRequest.getScopes() != null
				&& !oauthRequest.getScopes().equals("")) {
			queries.put(OAuth.OAUTH_SCOPE, oauthRequest.getScopes()
					.toString());
		}
		String urlWithQuery = buildUrlWithQuery(Constants.POST_AUTHORIZE_REDIRECT_URI, queries);
		LOGGER.info("urlWithQuery:<{}>", urlWithQuery);

		final OAuthResponse response = builder.location(urlWithQuery)
				.buildQueryMessage();
		URI url = null;
		try {
			url = new URI(urlWithQuery);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		return Response
				.status(response.getResponseStatus())
				.location(url)
				.build();
	}
	
	@Override
	@GET
	@Path("sdo_auth")
	public Response sdoAuthorize(@Context HttpServletRequest request)
			throws URISyntaxException, OAuthProblemException,
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

	private String buildUrlWithQuery(String baseUri, Map<String, String> queries) {
		StringBuilder uriBuilder = new StringBuilder(baseUri + "?");
		for (Entry<String, String> entry : queries.entrySet()) {
			if (entry.getValue() != null) {
				uriBuilder.append(entry.getKey())
						  .append("=")
						  .append(entry.getValue())
						  .append("&");
			} else {
				uriBuilder.append(entry.getKey()).append("&");
			}
		}
		return uriBuilder.toString().substring(0, uriBuilder.toString().length() - 1);
	}
	
	

}
