package com.snda.grand.space.as.rest.oauth2.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
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
import com.snda.grand.space.as.exception.InvalidAccessorException;
import com.snda.grand.space.as.exception.InvalidRefreshTokenException;
import com.snda.grand.space.as.exception.NoSuchAccountException;
import com.snda.grand.space.as.exception.NoSuchApplicationException;
import com.snda.grand.space.as.exception.NoSuchAuthorizationCodeException;
import com.snda.grand.space.as.exception.NoSuchRefreshTokenException;
import com.snda.grand.space.as.exception.RedirectUriMisatchException;
import com.snda.grand.space.as.exception.SdoValidateSignatureFailedException;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.mongo.model.PojoCode;
import com.snda.grand.space.as.mongo.model.PojoToken;
import com.snda.grand.space.as.rest.GrandSpaceOAuthTokenRequest;
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
import com.snda.grand.space.as.servlet.AccessorAuthenticateFilter;
import com.snda.grand.space.as.util.Merchant;


@Service
@Path("oauth2")
public class OAuth2ResourceImpl implements AuthorizationResource,
		TokenResource, ValidateResource, SdoAuthResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceImpl.class);
	
	private static Merchant merchant;
	private static MongoOperations mongoOps;
	private final ObjectMapper mapper = new ObjectMapper();
	private final OAuthIssuer oauthUUIDIssuer;
	private final OAuthIssuer oauthMD5Issuer;
	private final HttpClient httpClient = HttpClientUtils.getHttpClient();

	public OAuth2ResourceImpl(UUIDValueGenerator uuidGenerator,
			MD5Generator md5Generator, MongoOperations mongoOperations, Merchant merchant) {
		LOGGER.info("OAuth2ResourceImpl initialized.");
		this.oauthUUIDIssuer = new OAuthIssuerImpl(uuidGenerator);
		this.oauthMD5Issuer = new OAuthIssuerImpl(md5Generator);
		OAuth2ResourceImpl.mongoOps = mongoOperations;
		OAuth2ResourceImpl.merchant = merchant;
	}

	@Override
	@POST
	@Path("token")
	public Token exchangeToken(@Context HttpServletRequest request)
			throws OAuthProblemException, OAuthSystemException {
		GrandSpaceOAuthTokenRequest oauthRequest = new GrandSpaceOAuthTokenRequest(request);
		String appId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID);
		String signature = request.getHeader(HttpHeaders.AUTHORIZATION);
		LOGGER.debug(signature);

		PojoApplication pojoApplication = Preconditions.getApplicationByAppId(mongoOps, appId);
		if (pojoApplication == null) {
			throw new NoSuchApplicationException();
		}

		String accessToken = oauthMD5Issuer.accessToken();
		PojoAuthorization authorization = null;
		Preconditions.basicAuthorizationValidate(signature,
				pojoApplication.getAppKey(), pojoApplication.getAppSecret());
		// do checking for different grant types
		if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
				GrantType.AUTHORIZATION_CODE.toString())) {
			PojoCode pojoCode = Preconditions.getCode(mongoOps, oauthRequest.getCode());
			if (pojoCode == null) {
				throw new NoSuchAuthorizationCodeException();
			}
			else if (System.currentTimeMillis() > pojoCode.getCreationTime() + MongoCollections.CODE_EXPIRE_TIME) {
				Preconditions.deleteCode(mongoOps, pojoCode.getCode());
				throw new CodeExpiredException();
			} else if (pojoCode.getRedirectUri() != null 
					&& !pojoCode.getRedirectUri().equals(oauthRequest.getRedirectURI())) {
				throw new RedirectUriMisatchException();
			}
			authorization = Preconditions.getAuthorizationByUidAndAppId(
					mongoOps, pojoCode.getUid(), pojoCode.getAppId());
			if (authorization == null) {
				throw new NoSuchAuthorizationCodeException();
			}
			Preconditions.insertAccessToken(mongoOps,
					authorization.getRefreshToken(), accessToken,
					System.currentTimeMillis());
			Preconditions.deleteCode(mongoOps, pojoCode.getCode());
		} 
		// use refresh token to get access token
		else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
				GrantType.REFRESH_TOKEN.toString())) {
			authorization = Preconditions.getAuthorizationByRefreshToken(
					mongoOps, oauthRequest.getRefreshToken());
			if (authorization == null) {
				throw new NoSuchRefreshTokenException();
			}
			Preconditions.insertAccessToken(mongoOps,
					authorization.getRefreshToken(), accessToken,
					System.currentTimeMillis());
		}
		Token token = new Token()
							.setRefreshToken(authorization.getRefreshToken())
							.setAccessToken(accessToken)
							.setUid(authorization.getUid())
							.setExpireIn(MongoCollections.ACCESS_TOKEN_EXPIRE_TIME / 1000);
		return token;
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
		
		HttpResponse httpResponse = null;
		OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
		String sdoUnsignedValidateParams = oauthRequest.getParam(Constants.SDO_VALIDATE_URL_PARAM);
		LOGGER.info("Unsigned validate url:{}", sdoUnsignedValidateParams);
		
		try {
			HttpGet get = new HttpGet(createSdoValidateSignatureUrl(sdoUnsignedValidateParams));
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
				
				String redirectUri = oauthRequest.getRedirectURI();
				if (redirectUri != null) {
//					Preconditions.checkSubDomain(pojoApplication.getWebsite(),
//							redirectUri);
				}
				
				PojoAuthorization pojoAuthorization = Preconditions
						.getAuthorizationByUidAndAppId(mongoOps,
								pojoAccount.getUid(),
								pojoApplication.getAppid());
				if (pojoAuthorization == null) {
					// Create new Authorization for this uid to using the appid
					String refreshToken = oauthMD5Issuer.refreshToken();
					pojoAuthorization = new PojoAuthorization(
							pojoAccount.getUid(), pojoApplication.getAppid(),
							refreshToken, System.currentTimeMillis());
					Preconditions.insertAuthorization(mongoOps, pojoAuthorization);
				}
				
				String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
				LOGGER.info("Response type:<{}>", responseType);

				OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
						.authorizationResponse(request,
								HttpServletResponse.SC_FOUND);

				if (responseType.equals(ResponseType.CODE.toString())) {
					String code = oauthUUIDIssuer.authorizationCode();
					Preconditions.insertCode(mongoOps, code, redirectUri, 
							pojoAccount.getUid(), oauthRequest.getClientId());
					builder.setCode(code);
				} else if (responseType.equals(ResponseType.TOKEN.toString())) {
					String accessToken = oauthMD5Issuer.accessToken();
					Preconditions.insertAccessToken(mongoOps,
							pojoAuthorization.getRefreshToken(), accessToken,
							System.currentTimeMillis());
					builder.setAccessToken(accessToken);
					builder.setExpiresIn(MongoCollections.ACCESS_TOKEN_EXPIRE_TIME / 1000);
				}
				
				final OAuthResponse response = builder
						.location(
								(redirectUri == null ? Constants.DEFAULT_AUTHORIZE_SUCCESS_REDIRECT_URI
										: redirectUri)).buildQueryMessage();
				URI url = new URI(response.getLocationUri());

				return Response
						.status(response.getResponseStatus())
						.location(url)
						.build();
			} else {
				throw new SdoValidateSignatureFailedException();
			}
			
		} finally {
			responseContentConsume(httpResponse);
		}
	}
	
	@Override
	@POST
	@Path("validate")
	public Validation validate(@Context HttpServletRequest request) {
		String accessToken = request.getParameter("access_token");
		checkValidateBasicAuth(request.getParameter("authorization"));
		
		PojoToken token = Preconditions.getTokenByAccessToken(mongoOps, accessToken);
		if (token == null) {
			throw new InvalidAccessTokenException();
		} else if (token.getExpire() < System.currentTimeMillis()) {
			Preconditions.deleteTokenByAccessToken(mongoOps, token.getAccessToken());
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
	
	private void checkValidateBasicAuth(String authorization) {
		String credential = AccessorAuthenticateFilter
				.getCredential(authorization);
		String[] pair = credential.split(":");
		if (pair.length != 2) {
			throw new InvalidAccessorException();
		}
		String accessKey = pair[0];
		String secretKey = pair[1];
		if (Preconditions.getAccessor(mongoOps, accessKey, secretKey) == null) {
			throw new InvalidAccessorException();
		}
	}
	
	private String createSdoValidateSignatureUrl(String params) {
		List<String> list = Preconditions.getQueriesFromQueryString(params);
		List<String> canonicalList = Preconditions.getSdoValidateCanonicalQueryList(merchant, list);
		return Preconditions.makeSignedSdoValidateUrl(merchant, canonicalList);
	}

}
