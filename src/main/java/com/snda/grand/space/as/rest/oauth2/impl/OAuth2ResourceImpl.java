package com.snda.grand.space.as.rest.oauth2.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.amber.oauth2.common.message.types.ParameterStyle;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.apache.amber.oauth2.common.utils.OAuthUtils;
import org.apache.amber.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.amber.oauth2.rs.response.OAuthRSResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.mongo.model.PojoCode;
import com.snda.grand.space.as.mongo.model.PojoToken;
import com.snda.grand.space.as.rest.model.Validation;
import com.snda.grand.space.as.rest.oauth2.AuthorizationResource;
import com.snda.grand.space.as.rest.oauth2.TokenResource;
import com.snda.grand.space.as.rest.oauth2.ValidateResource;
import com.snda.grand.space.as.rest.util.Common;
import com.snda.grand.space.as.rest.util.Constants;


@Service
@Path("oauth2")
public class OAuth2ResourceImpl implements AuthorizationResource, TokenResource, ValidateResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceImpl.class);
	
	private static MongoOperations mongoOps;
	private final OAuthIssuer oauthUUIDIssuer;
	private final OAuthIssuer oauthMD5Issuer;

	public OAuth2ResourceImpl(UUIDValueGenerator uuidGenerator,
			MD5Generator md5Generator, MongoOperations mongoOperations) {
		LOGGER.info("OAuth2ResourceImpl initialized.");
		this.oauthUUIDIssuer = new OAuthIssuerImpl(uuidGenerator);
		this.oauthMD5Issuer = new OAuthIssuerImpl(md5Generator);
		OAuth2ResourceImpl.mongoOps = mongoOperations;
	}

	@Override
	@POST
	@Path("access_token")
	@Produces("application/json")
	public Response exchangeToken(@Context HttpServletRequest request)
			throws OAuthSystemException {
		OAuthTokenRequest oauthRequest = null;
		String refreshToken = null;
		String accessToken = null;

		try {
			oauthRequest = new OAuthTokenRequest(request);

			if (!checkApplicationExist(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID))) {
				OAuthResponse response = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_NOT_FOUND)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription("client_id not found")
						.buildJSONMessage();
				return Response
						.status(response.getResponseStatus())
						.entity(response.getBody())
						.build();
			}

			// do checking for different grant types
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				PojoCode code = mongoOps.findOne(
						query(where(Collections.Code.CODE).is(
								oauthRequest.getCode())), PojoCode.class,
						Collections.CODE_COLLECTION_NAME);
				if (code == null) {
					OAuthResponse response = OAuthASResponse
							.errorResponse(HttpServletResponse.SC_NOT_FOUND)
							.setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription("no such authorization code")
							.buildJSONMessage();
					return Response.status(response.getResponseStatus())
							.entity(response.getBody()).build();
				}
				else if (System.currentTimeMillis() > code.getCreationTime() + Collections.CODE_EXPIRE_TIME) {
					OAuthResponse response = OAuthASResponse
							.errorResponse(HttpServletResponse.SC_FORBIDDEN)
							.setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription("authorization code expired")
							.buildJSONMessage();
					return Response.status(response.getResponseStatus())
							.entity(response.getBody()).build();
				}
				refreshToken = oauthMD5Issuer.accessToken();
			} 
			// individuality account, need to record the username and password
			else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
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
				refreshToken = oauthMD5Issuer.accessToken();
			}
			// use refresh token to get access token
			else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.REFRESH_TOKEN.toString())) {
				if (!checkRefreshTokenExist(oauthRequest.getClientId(),
						oauthRequest.getRefreshToken())) {
					OAuthResponse response = OAuthASResponse
							.errorResponse(HttpServletResponse.SC_NOT_FOUND)
							.setError(OAuthError.ResourceResponse.INVALID_TOKEN)
							.setErrorDescription("invalid refresh token")
							.buildJSONMessage();
					return Response
							.status(response.getResponseStatus())
							.entity(response.getBody())
							.build();
				}
			}

			accessToken = oauthMD5Issuer.accessToken();
			OAuthResponse response = null;
			if (refreshToken == null) {
				response = OAuthASResponse
						.tokenResponse(HttpServletResponse.SC_OK)
						.setAccessToken(accessToken)
						.setExpiresIn("3600").buildJSONMessage();
				insertAccessToken(oauthRequest.getRefreshToken(), accessToken);
			} else {
				response = OAuthASResponse
						.tokenResponse(HttpServletResponse.SC_OK)
						.setAccessToken(accessToken)
						.setRefreshToken(refreshToken)
						.setExpiresIn("3600").buildJSONMessage();
				insertRefreshToken(getApplicationByAppId(oauthRequest.getClientId())
						.getAppid(), oauthRequest.getClientId(), refreshToken);
				insertAccessToken(refreshToken, accessToken);
			}

			return Response
					.status(response.getResponseStatus())
					.entity(response.getBody())
					.build();
		} catch (OAuthProblemException e) {
			OAuthResponse res = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return Response
					.status(res.getResponseStatus())
					.entity(res.getBody())
					.build();
		}
	}

	@Override
	@POST
	@Path("authorize")
	@Produces("application/json")
	public Response authorize(@Context HttpServletRequest request)
			throws URISyntaxException, OAuthSystemException {
		OAuthAuthzRequest oauthRequest = null;
		
		try {
			oauthRequest = new OAuthAuthzRequest(request);

			// build response according to response_type
			String responseType = oauthRequest
					.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			LOGGER.info("Response type:<{}>", responseType);

			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
					.authorizationResponse(request,
							HttpServletResponse.SC_FOUND);

			if (responseType.equals(ResponseType.CODE.toString())) {
				String code = oauthUUIDIssuer.authorizationCode();
				insertCode(code);
				builder.setCode(code);
			}
			
			if (responseType.equals(ResponseType.TOKEN.toString())) {
				builder.setAccessToken(oauthUUIDIssuer.accessToken());
				builder.setExpiresIn(3600L);
			}

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
			URI url = new URI(urlWithQuery);

			return Response
					.status(response.getResponseStatus())
					.location(url)
					.build();

		} catch (OAuthProblemException e) {

			final Response.ResponseBuilder responseBuilder = Response
					.status(HttpServletResponse.SC_FOUND);

			String redirectUri = e.getRedirectUri();

			if (OAuthUtils.isEmpty(redirectUri)) {
				throw new WebApplicationException(responseBuilder.entity(
						"OAuth callback url needs to be provided by client!!!")
						.build());
			}
			final OAuthResponse response = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_FOUND).error(e)
					.location(redirectUri).buildQueryMessage();
			final URI location = new URI(response.getLocationUri());
			return responseBuilder.location(location).build();
		}
	}
	
	@Override
	@GET
	@Path("validate")
	public Validation validate(@Context HttpServletRequest request)
			throws OAuthSystemException {
		try {

			// Make the OAuth Request out of this request
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(
					request, ParameterStyle.QUERY);

			// Get the access token
			String accessToken = oauthRequest.getAccessToken();

			PojoToken token = getToken(accessToken);
			if (token == null) {
				// TODO
				// throw invalid access token exception
			} else if (token.getExpire() > System.currentTimeMillis()) {
				// TODO
				// throw expire exception
			}
			PojoAuthorization authorization = getAuthorizationByRefreshToken(token
					.getRefreshToken());
			PojoApplication application = getApplicationByAppId(authorization
					.getAppId());
			PojoAccount account = getAccountByUid(authorization.getUid());
			Validation validation = new Validation(account.getUsernameNorm(),
					application.getScope(), token.getAccessToken(),
					token.getExpire());
			return validation;
			// Validate the access token
//			if (!checkAccessTokenExist(accessToken)) {
//
//				// Return the OAuth error message
//				OAuthResponse oauthResponse = OAuthRSResponse
//						.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
//						.setRealm(Common.RESOURCE_SERVER_NAME)
//						.setError(OAuthError.ResourceResponse.INVALID_TOKEN)
//						.buildHeaderMessage();
//
//				// return Response.status(Response.Status.UNAUTHORIZED).build();
//				return Response
//						.status(Response.Status.UNAUTHORIZED)
//						.header(OAuth.HeaderType.WWW_AUTHENTICATE,
//								oauthResponse
//										.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
//						.build();
//
//			
//			}

		} catch (OAuthProblemException e) {
			// Check if the error code has been set
			String errorCode = e.getError();
			if (OAuthUtils.isEmpty(errorCode)) {

				// Return the OAuth error message
				OAuthResponse oauthResponse = OAuthRSResponse
						.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setRealm(Common.RESOURCE_SERVER_NAME)
						.buildHeaderMessage();

				// If no error code then return a standard 401 Unauthorized
				// response
//				return Response
//						.status(Response.Status.UNAUTHORIZED)
//						.header(OAuth.HeaderType.WWW_AUTHENTICATE,
//								oauthResponse
//										.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
//						.build();
				return null;
			}

			OAuthResponse oauthResponse = OAuthRSResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setRealm(Common.RESOURCE_SERVER_NAME)
					.setError(e.getError())
					.setErrorDescription(e.getDescription())
					.setErrorUri(e.getUri()).buildHeaderMessage();

//			return Response
//					.status(Response.Status.BAD_REQUEST)
//					.header(OAuth.HeaderType.WWW_AUTHENTICATE,
//							oauthResponse
//									.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
//					.build();
			return null;
		}
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
	
	private PojoAccount getAccountByUid(String uid) {
		PojoAccount account = mongoOps.findOne(
				query(where(Collections.Account.UID).is(uid)),
				PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Account : {}", account);
		}
		return account;
	}
	
	private PojoApplication getApplicationByAppId(String appId) {
		PojoApplication app = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)),
				PojoApplication.class, Collections.APPLICATION_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Application : {}", app);
		}
		return app;
	}
	
	private boolean checkApplicationExist(String appId) {
		return getApplicationByAppId(appId) != null;
	}
	
	private PojoAuthorization getAuthorizationByRefreshToken(String refreshToken) {
		PojoAuthorization authorization = mongoOps.findOne(
				query(where(Collections.Authorization.REFRESH_TOKEN).is(
						refreshToken)), PojoAuthorization.class,
				Collections.AUTHORIZATION_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Authorization : {}", authorization);
		}
		return authorization;
	}
	
	private PojoToken getToken(String accessToken) {
		PojoToken token = mongoOps.findOne(
				query(where(Collections.Token.ACCESS_TOKEN).is(accessToken)),
				PojoToken.class, Collections.TOKEN_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Token : {}", token);
		}
		return token;
	}
	
	private boolean checkRefreshTokenExist(String appId, String refreshToken) {
		Query query = new Query();
		query.addCriteria(where(Collections.Authorization.APPID).is(appId))
			 .addCriteria(where(Collections.Authorization.REFRESH_TOKEN).is(refreshToken));
		PojoAuthorization authorization = mongoOps.findOne(query,
				PojoAuthorization.class, Collections.AUTHORIZATION_COLLECTION_NAME);
		return authorization != null;
	}
	
	private void insertRefreshToken(String uid, String appId, String refreshToken) {
		PojoAuthorization authorization = new PojoAuthorization(uid, appId,
				refreshToken, System.currentTimeMillis());
		mongoOps.insert(authorization, Collections.AUTHORIZATION_COLLECTION_NAME);
	}
	
	private void insertAccessToken(String refreshToken, String accessToken) {
		long creationTime = System.currentTimeMillis();
		PojoToken token = new PojoToken(refreshToken, accessToken, creationTime,
				creationTime + 3600000);
		mongoOps.insert(token, Collections.TOKEN_COLLECTION_NAME);
	}
	
	private void insertCode(String codeString) {
		PojoCode code = new PojoCode(codeString, System.currentTimeMillis());
		mongoOps.insert(code, Collections.CODE_COLLECTION_NAME);
	}

}
