package com.snda.grand.space.as.rest.oauth2.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import javax.ws.rs.QueryParam;
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
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.snda.grand.space.as.exception.AccessTokenExpiredException;
import com.snda.grand.space.as.exception.CodeExpiredException;
import com.snda.grand.space.as.exception.InvalidAccessTokenException;
import com.snda.grand.space.as.exception.InvalidRefreshTokenException;
import com.snda.grand.space.as.exception.NoSuchAccountException;
import com.snda.grand.space.as.exception.NoSuchApplicationException;
import com.snda.grand.space.as.exception.NoSuchAuthorizationCodeException;
import com.snda.grand.space.as.exception.NoSuchRefreshTokenException;
import com.snda.grand.space.as.exception.SdoValidateSignatureException;
import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.mongo.model.PojoCode;
import com.snda.grand.space.as.mongo.model.PojoToken;
import com.snda.grand.space.as.rest.model.SdoValidation;
import com.snda.grand.space.as.rest.model.Token;
import com.snda.grand.space.as.rest.model.Validation;
import com.snda.grand.space.as.rest.oauth2.AuthorizationResource;
import com.snda.grand.space.as.rest.oauth2.SdoAuthResource;
import com.snda.grand.space.as.rest.oauth2.TokenResource;
import com.snda.grand.space.as.rest.oauth2.ValidateResource;
import com.snda.grand.space.as.rest.util.Constants;
import com.snda.grand.space.as.rest.util.HttpClientUtils;
import com.snda.grand.space.as.rest.util.ObjectMappers;
import com.snda.grand.space.as.rest.util.Preconditions;


@Service
@Path("oauth2")
public class OAuth2ResourceImpl implements AuthorizationResource,
		TokenResource, ValidateResource, SdoAuthResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceImpl.class);
	
	private static MongoOperations mongoOps;
	private final ObjectMapper mapper = new ObjectMapper();
	private final OAuthIssuer oauthUUIDIssuer;
	private final OAuthIssuer oauthMD5Issuer;
	private final HttpClient httpClient = HttpClientUtils.getHttpClient();

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
	public Token exchangeToken(@Context HttpServletRequest request)
			throws OAuthProblemException, OAuthSystemException {

		OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
		String appId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID);

		PojoApplication pojoApplication = Preconditions.getApplicationByAppId(mongoOps, appId);
		if (pojoApplication == null) {
			throw new NoSuchApplicationException();
		}

		String accessToken = oauthMD5Issuer.accessToken();
		PojoAuthorization authorization = null;
		// do checking for different grant types
		if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
				GrantType.AUTHORIZATION_CODE.toString())) {
			PojoCode code = Preconditions.getCode(mongoOps, oauthRequest.getCode());
			if (code == null) {
				throw new NoSuchAuthorizationCodeException();
			}
			else if (System.currentTimeMillis() > code.getCreationTime() + Collections.CODE_EXPIRE_TIME) {
				throw new CodeExpiredException();
			}
			authorization = Preconditions.getAuthorizationByUidAndAppId(
					mongoOps, code.getUid(), code.getAppId());
			if (authorization == null) {
				throw new NoSuchAuthorizationCodeException();
			}
			insertAccessToken(authorization.getRefreshToken(), accessToken,
					System.currentTimeMillis());
		} 
		// use refresh token to get access token
		else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
				GrantType.REFRESH_TOKEN.toString())) {
			authorization = Preconditions.getAuthorizationByRefreshToken(
					mongoOps, oauthRequest.getRefreshToken());
			if (authorization == null) {
				throw new NoSuchRefreshTokenException();
			}
			insertAccessToken(authorization.getRefreshToken(), accessToken,
					System.currentTimeMillis());
		}
		Token token = new Token();
		token.setAccessToken(accessToken);
		token.setUid(authorization.getUid());
		token.setExpireIn(Collections.ACCESS_TOKEN_EXPIRE_TIME);
		return token;
	}

	@Override
	@POST
	@Path("authorize")
	@Produces("application/json")
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
	@Produces("application/json")
	public Response sdoAuthorize(@Context HttpServletRequest request)
			throws URISyntaxException, OAuthProblemException,
			OAuthSystemException, IOException {
		
		HttpResponse httpResponse = null;
		OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
		String sdoValidateURL = oauthRequest.getParam(Constants.SDO_VALIDATE_URL_HEADER);
		LOGGER.info("Validate url:{}", sdoValidateURL);
		
		try {
			HttpGet get = new HttpGet(sdoValidateURL);
			httpResponse = httpClient.execute(get);
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						httpResponse.getEntity().getContent()));
				String line = null;
				while ( (line = br.readLine()) != null) {
					LOGGER.info("sdo validate:{}", line);
					sb.append(line);
				}
				SdoValidation sdoValidation = ObjectMappers.readJSON(mapper,
						sb.toString(), SdoValidation.class);
				PojoAccount pojoAccount = Preconditions.getAccountBySndaId(
						mongoOps, sdoValidation.getData().getSndaId());
				if (pojoAccount == null) {
					throw new NoSuchAccountException();
				}
				
				PojoApplication pojoApplication = Preconditions
						.getApplicationByAppId(mongoOps,
								oauthRequest.getClientId());
				if (pojoApplication == null) {
					throw new NoSuchApplicationException();
				}
				
				PojoAuthorization pojoAuthorization = Preconditions
						.getAuthorizationByUidAndAppId(mongoOps,
								pojoAccount.getUid(),
								pojoApplication.getAppid());
				if (pojoAuthorization == null) {
					// Create new Authorization for this uid to using the appid
					Preconditions.insertRefreshToken(mongoOps,
							pojoAccount.getUid(), pojoApplication.getAppid(),
							oauthMD5Issuer.refreshToken());
				}
				
				String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
				LOGGER.info("Response type:<{}>", responseType);

				OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
						.authorizationResponse(request,
								HttpServletResponse.SC_FOUND);

				if (responseType.equals(ResponseType.CODE.toString())) {
					String code = oauthUUIDIssuer.authorizationCode();
					insertCode(code, pojoAccount.getUid(), oauthRequest.getClientId());
					builder.setCode(code);
				}
				
				final OAuthResponse response = builder.location(oauthRequest.getRedirectURI())
						.buildQueryMessage();
				URI url = new URI(response.getLocationUri());

				return Response
						.status(response.getResponseStatus())
						.location(url)
						.build();
			} else {
				throw new SdoValidateSignatureException();
			}
			
		} finally {
			responseContentConsume(httpResponse);
		}
	}
	
	@Override
	@GET
	@Path("validate")
	public Validation validate(@QueryParam("access_token") String accessToken) {

		PojoToken token = Preconditions.getTokenByAccessToken(mongoOps, accessToken);
		if (token == null) {
			throw new InvalidAccessTokenException();
		} else if (token.getExpire() < System.currentTimeMillis()) {
			throw new AccessTokenExpiredException();
		}
		
		PojoAuthorization authorization = Preconditions
				.getAuthorizationByRefreshToken(mongoOps,
						token.getRefreshToken());
		if (authorization == null) {
			throw new InvalidRefreshTokenException();
		}
		
		PojoApplication application = Preconditions.getApplicationByAppId(
				mongoOps, authorization.getAppId());
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		
		PojoAccount account = Preconditions.getAccountByUid(mongoOps, authorization.getUid());
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Validation validation = new Validation(account.getSndaId(),
				application.getAppid(), application.getScope(),
				token.getAccessToken(), token.getExpire());
		
		return validation;
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
	
	private void insertAccessToken(String refreshToken, String accessToken, long creationTime) {
		PojoToken token = new PojoToken(refreshToken, accessToken, creationTime,
				creationTime + Collections.ACCESS_TOKEN_EXPIRE_TIME);
		mongoOps.insert(token, Collections.TOKEN_COLLECTION_NAME);
	}
	
	private void insertCode(String code, String uid, String appId) {
		PojoCode pojoCode = new PojoCode(code, uid, appId,
				System.currentTimeMillis());
		mongoOps.insert(pojoCode, Collections.CODE_COLLECTION_NAME);
	}
	
	private void responseContentConsume(HttpResponse response) {
		try {
			if (response != null && response.getEntity() != null) {
				EntityUtils.consume(response.getEntity());
			}
		} catch (IOException e) {
			LOGGER.error("Response content consume error", e);
			throw new IllegalStateException(e);
		}
	}

}
