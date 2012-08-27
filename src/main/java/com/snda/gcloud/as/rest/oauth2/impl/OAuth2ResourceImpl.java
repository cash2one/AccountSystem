package com.snda.gcloud.as.rest.oauth2.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.apache.amber.oauth2.common.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.snda.gcloud.as.rest.oauth2.AuthorizationResource;
import com.snda.gcloud.as.rest.oauth2.TokenResource;
import com.snda.gcloud.as.rest.util.Common;
import com.snda.gcloud.as.rest.util.Constants;


@Service
@Path("oauth2")
public class OAuth2ResourceImpl implements AuthorizationResource, TokenResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceImpl.class);
	
	private final OAuthIssuer oauthIssuer;

	public OAuth2ResourceImpl(OAuthIssuer oauthIssuer) {
		LOGGER.info("OAuth2ResourceImpl initialized.");
		this.oauthIssuer = oauthIssuer;
	}

	@Override
	@POST
	@Path("access_token")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response exchangeToken(@Context HttpServletRequest request)
			throws OAuthSystemException {
		OAuthTokenRequest oauthRequest = null;

		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

		try {
			oauthRequest = new OAuthTokenRequest(request);

			// check if clientid is valid
			if (!Common.CLIENT_ID.equals(oauthRequest
					.getParam(OAuth.OAUTH_CLIENT_ID))) {
				OAuthResponse response = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription("client_id not found")
						.buildJSONMessage();
				return Response.status(response.getResponseStatus())
						.entity(response.getBody()).build();
			}

			// do checking for different grant types
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				if (!Common.AUTHORIZATION_CODE.equals(oauthRequest
						.getParam(OAuth.OAUTH_CODE))) {
					OAuthResponse response = OAuthASResponse
							.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription("invalid authorization code")
							.buildJSONMessage();
					return Response.status(response.getResponseStatus())
							.entity(response.getBody()).build();
				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.PASSWORD.toString())) {
				if (!Common.PASSWORD.equals(oauthRequest.getPassword())
						|| !Common.USERNAME.equals(oauthRequest.getUsername())) {
					OAuthResponse response = OAuthASResponse
							.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription("invalid username or password")
							.buildJSONMessage();
					return Response.status(response.getResponseStatus())
							.entity(response.getBody()).build();
				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.REFRESH_TOKEN.toString())) {
				OAuthResponse response = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_GRANT)
						.setErrorDescription("invalid username or password")
						.buildJSONMessage();
				return Response.status(response.getResponseStatus())
						.entity(response.getBody()).build();
			}

			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(oauthIssuerImpl.accessToken())
					.setExpiresIn("3600").buildJSONMessage();

			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();
		} catch (OAuthProblemException e) {
			OAuthResponse res = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return Response.status(res.getResponseStatus())
					.entity(res.getBody()).build();
		}
	}

	@Override
	@GET
	@Path("access_token")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response exchangeTokenGet(@Context HttpServletRequest request)
			throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse
				.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(oauthIssuer.accessToken()).setExpiresIn("3600")
				.buildJSONMessage();

		return Response.status(response.getResponseStatus())
				.entity(response.getBody()).build();
	}
	
	@Override
	@GET
	@Path("authorize")
	public Response authorizeGet(@Context HttpServletRequest request) 
			throws URISyntaxException, OAuthSystemException {
		OAuthAuthzRequest oauthRequest = null;
		
		try {
            oauthRequest = new OAuthAuthzRequest(request);

            //build response according to response_type
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            LOGGER.info("Response type:<{}>", responseType);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                .authorizationResponse(request,HttpServletResponse.SC_FOUND);

            if (responseType.equals(ResponseType.CODE.toString())) {
                builder.setCode(oauthIssuer.authorizationCode());
            }
            if (responseType.equals(ResponseType.TOKEN.toString())) {
                builder.setAccessToken(oauthIssuer.accessToken());
                builder.setExpiresIn(3600L);
            }

            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            LOGGER.info("Redirect URI:<{}>", redirectURI);

            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
            URI url = new URI(response.getLocationUri());

            return Response.status(response.getResponseStatus()).location(url).build();

        } catch (OAuthProblemException e) {

            final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);

            String redirectUri = e.getRedirectUri();

            if (OAuthUtils.isEmpty(redirectUri)) {
                throw new WebApplicationException(
                    responseBuilder.entity("OAuth callback url needs to be provided by client!!!").build());
            }
            final OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                .error(e)
                .location(redirectUri).buildQueryMessage();
            final URI location = new URI(response.getLocationUri());
            return responseBuilder.location(location).build();
        }
    }

	@Override
	@POST
	@Path("authorize")
	public Response authorize(@QueryParam("client_id") String clientId, 
			@QueryParam("response_type") String responseType,
			@QueryParam("redirect_uri") String redirectUri, 
			@QueryParam("scope") String scope) throws URISyntaxException {
		HashMap<String, String> queries = Maps.newHashMap();
		if (clientId != null && !clientId.equalsIgnoreCase("")) {
			queries.put(Constants.CLIENT_ID, clientId);
		}
		if (responseType != null && !responseType.equalsIgnoreCase("")) {
			queries.put(Constants.RESPONSE_TYPE, responseType);
		}
		if (redirectUri != null && !redirectUri.equalsIgnoreCase("")) {
			queries.put(Constants.REDIRECT_URI, redirectUri);
		}
		if (scope != null && !scope.equalsIgnoreCase("")) {
			queries.put(Constants.SCOPE, scope);
		}
		String urlWithQuery = buildUrlWithQuery(Constants.POST_AUTHORIZE_REDIRECT_URI, queries);
		LOGGER.info("urlWithQuery:<{}>", urlWithQuery);
		URI location = new URI(urlWithQuery);
		return Response.status(Status.SEE_OTHER)
				.location(location)
				.build();
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
