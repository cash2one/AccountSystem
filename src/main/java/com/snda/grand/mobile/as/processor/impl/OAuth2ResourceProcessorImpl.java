package com.snda.grand.mobile.as.processor.impl;

import static com.snda.grand.mobile.as.rest.util.Preconditions.buildUrlWithQuery;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAccessTokenNotExpired;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAccountNotNull;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkClientNotValid;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkCodeNotExpired;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkGrantNotValid;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkRedirectUrlNotMatch;
import static com.snda.grand.mobile.as.rest.util.Preconditions.createSdoValidateSignatureUrl;
import static com.snda.grand.mobile.as.rest.util.Preconditions.responseContentConsume;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.snda.grand.mobile.as.account.AccessorService;
import com.snda.grand.mobile.as.account.AccountService;
import com.snda.grand.mobile.as.account.ApplicationService;
import com.snda.grand.mobile.as.account.AuthorizationService;
import com.snda.grand.mobile.as.account.CodeService;
import com.snda.grand.mobile.as.account.TokenService;
import com.snda.grand.mobile.as.exception.ASOAuthAccessDeniedException;
import com.snda.grand.mobile.as.exception.ASOAuthDomainMismatchException;
import com.snda.grand.mobile.as.exception.ASOAuthProblemException;
import com.snda.grand.mobile.as.exception.ASOAuthUnauthorizedClientException;
import com.snda.grand.mobile.as.exception.InvalidAccessorException;
import com.snda.grand.mobile.as.exception.SdoValidateSignatureFailedException;
import com.snda.grand.mobile.as.mongo.model.MongoCollections;
import com.snda.grand.mobile.as.mongo.model.PojoAuthorization;
import com.snda.grand.mobile.as.mongo.model.PojoCode;
import com.snda.grand.mobile.as.mongo.model.PojoToken;
import com.snda.grand.mobile.as.processor.OAuth2ResourceProcessor;
import com.snda.grand.mobile.as.rest.GrandMobileOAuthAuthzRequest;
import com.snda.grand.mobile.as.rest.GrandMobileOAuthTokenRequest;
import com.snda.grand.mobile.as.rest.model.Account;
import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.rest.model.SdoValidation;
import com.snda.grand.mobile.as.rest.model.Token;
import com.snda.grand.mobile.as.rest.model.Validation;
import com.snda.grand.mobile.as.rest.util.ApplicationKeys;
import com.snda.grand.mobile.as.rest.util.Constants;
import com.snda.grand.mobile.as.rest.util.HttpClientUtils;
import com.snda.grand.mobile.as.rest.util.ObjectMappers;
import com.snda.grand.mobile.as.rest.util.Preconditions;
import com.snda.grand.mobile.as.servlet.AccessorAuthenticateFilter;
import com.snda.grand.mobile.as.util.Merchant;

public class OAuth2ResourceProcessorImpl implements OAuth2ResourceProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceProcessorImpl.class);
	
	private final AccountService accountService;
	private final ApplicationService applicationService;
	private final AuthorizationService authorizationService;
	private final TokenService tokenService;
	private final CodeService codeService;
	private final AccessorService accessorService;
	private final OAuthIssuer oauthUUIDIssuer;
	private final OAuthIssuer oauthMD5Issuer;
	private final Merchant merchant;
	private final ObjectMapper mapper = new ObjectMapper();
	private final HttpClient httpClient = HttpClientUtils.getHttpClient();
	
	public OAuth2ResourceProcessorImpl(AccountService accountService,
			ApplicationService applicationService,
			AuthorizationService authorizationService,
			TokenService tokenService,
			CodeService codeService,
			AccessorService accessorService,
			UUIDValueGenerator uuidGenerator,
			MD5Generator md5Generator,
			Merchant merchant) {
		this.accountService = accountService;
		this.applicationService = applicationService;
		this.authorizationService = authorizationService;
		this.tokenService = tokenService;
		this.codeService = codeService;
		this.accessorService = accessorService;
		this.oauthUUIDIssuer = new OAuthIssuerImpl(uuidGenerator);
		this.oauthMD5Issuer = new OAuthIssuerImpl(md5Generator);
		this.merchant = merchant;
	}
	
	@Override
	public Response authorize(HttpServletRequest request)
			throws ASOAuthProblemException, OAuthSystemException {
		GrandMobileOAuthAuthzRequest oauthRequest;
		try {
			oauthRequest = new GrandMobileOAuthAuthzRequest(request);
		} catch (OAuthProblemException e) {
			throw new ASOAuthProblemException(e, "authorize");
		}

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
				&& oauthRequest.getScopes().size() > 0) {
			queries.put(OAuth.OAUTH_SCOPE, oauthRequest.getScopes().toArray()[0].toString());
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
	public Response sdoAuthorize(HttpServletRequest request)
			throws URISyntaxException, ASOAuthProblemException,
			OAuthSystemException, IOException {
		HttpResponse httpResponse = null;
		GrandMobileOAuthAuthzRequest oauthRequest;
		try {
			oauthRequest = new GrandMobileOAuthAuthzRequest(request);
		} catch (OAuthProblemException e) {
			throw new ASOAuthProblemException(e, "authorize");
		}
		String sdoUnsignedValidateParams = oauthRequest.getParam(Constants.SDO_VALIDATE_URL_PARAM);
		LOGGER.info("Unsigned validate url:{}", sdoUnsignedValidateParams);
		
		try {
			HttpGet get = new HttpGet(createSdoValidateSignatureUrl(merchant, sdoUnsignedValidateParams));
			httpResponse = httpClient.execute(get);
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						httpResponse.getEntity().getContent()));
				String line = null;
				while ( (line = br.readLine()) != null) {
					LOGGER.debug("sdo validate:{}", line);
					sb.append(line);
				}
				SdoValidation sdoValidation = ObjectMappers.readJSON(mapper,
						sb.toString(), SdoValidation.class);
				String redirectUri = oauthRequest.getRedirectURI();
				if (redirectUri == null) {
					redirectUri = Constants.DEFAULT_AUTHORIZE_SUCCESS_REDIRECT_URI;
				}
				Account account = getOrCreateAccount(sdoValidation);
				Application application = applicationService.getApplicationByAppId(oauthRequest.getClientId());
				if (application == null) {
					throw ASOAuthUnauthorizedClientException.build("authorize", redirectUri);
				}
				if (oauthRequest.getRedirectURI() != null && application.getWebsite() != null) {
					if (!Preconditions.checkSubDomain(application.getWebsite(), oauthRequest.getRedirectURI())) {
						throw ASOAuthDomainMismatchException.build("authorize", redirectUri);
					}
				}
				
				String scope = application.getScope();
				PojoAuthorization pojoAuthorization = authorizationService
						.getAuthorizationByUidAndAppId(account.getUid(),
								application.getAppid());
				if (pojoAuthorization == null) {
					// Create new Authorization for this uid to using the appid
					String refreshToken = oauthMD5Issuer.refreshToken();
					pojoAuthorization = new PojoAuthorization(
							account.getUid(), application.getAppid(),
							refreshToken, System.currentTimeMillis(), scope);
					authorizationService.putAuthorization(pojoAuthorization);
				} else if (!pojoAuthorization.getAuthorizedScope().equalsIgnoreCase(scope)) {
					authorizationService.updateAuthorizationScope(
							pojoAuthorization.getRefreshToken(), scope);
				}
				
				String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
				LOGGER.info("Response type:<{}>", responseType);

				OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
						.authorizationResponse(request,
								HttpServletResponse.SC_FOUND);

				if (responseType.equals(ResponseType.CODE.toString())) {
					String code = oauthUUIDIssuer.authorizationCode();
					codeService.putCode(code, redirectUri, account.getUid(), oauthRequest.getClientId());
					builder.setCode(code);
				} else if (responseType.equals(ResponseType.TOKEN.toString())) {
					String accessToken = oauthMD5Issuer.accessToken();
					long creationTime = System.currentTimeMillis();
					tokenService
							.putToken(
									pojoAuthorization.getRefreshToken(),
									accessToken,
									creationTime,
									creationTime
											+ MongoCollections.ACCESS_TOKEN_EXPIRE_TIME);
					builder.setAccessToken(accessToken);
					builder.setExpiresIn(MongoCollections.ACCESS_TOKEN_EXPIRE_TIME / 1000);
				}
				final OAuthResponse response = builder.location(redirectUri)
						.buildQueryMessage();
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
	public Response denyAuthorize(HttpServletRequest resquest)
			throws ASOAuthProblemException {
		String redirectUri = resquest.getParameter("redirect_uri");
		if (redirectUri == null) {
			redirectUri = Constants.DEFAULT_AUTHORIZE_SUCCESS_REDIRECT_URI;
		}
		throw ASOAuthAccessDeniedException.build("authorize", redirectUri);
	}

	@Override
	public Token exchangeToken(HttpServletRequest request)
			throws ASOAuthProblemException, OAuthSystemException {
		GrandMobileOAuthTokenRequest oauthRequest;
		try {
			oauthRequest = new GrandMobileOAuthTokenRequest(request);
		} catch (OAuthProblemException e) {
			throw new ASOAuthProblemException(e, "token");
		}
		String appId = oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID);
		String signature = request.getHeader(HttpHeaders.AUTHORIZATION);

		Application application = checkClientNotValid(applicationService.getApplicationByAppId(appId));

		String accessToken = oauthMD5Issuer.accessToken();
		PojoAuthorization authorization = null;
		Preconditions.basicAuthorizationValidate(signature,
				application.getAppKey(), application.getAppSecret());
		// do checking for different grant types
		if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
				GrantType.AUTHORIZATION_CODE.toString())) {
			PojoCode pojoCode = checkGrantNotValid(codeService.getCode(oauthRequest.getCode()));
			checkCodeNotExpired(pojoCode);
			checkRedirectUrlNotMatch(pojoCode, oauthRequest.getRedirectURI());
			codeService.deleteCode(oauthRequest.getCode());
			authorization = checkGrantNotValid(authorizationService.getAuthorizationByUidAndAppId(pojoCode.getUid(), pojoCode.getAppId()));
			long creationTime = System.currentTimeMillis();
			tokenService.putToken(authorization.getRefreshToken(), accessToken,
					creationTime, creationTime + MongoCollections.ACCESS_TOKEN_EXPIRE_TIME);
			codeService.deleteCode(pojoCode.getCode());
		} 
		// use refresh token to get access token
		else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
				GrantType.REFRESH_TOKEN.toString())) {
			authorization = checkGrantNotValid(authorizationService.getAuthorizationByRefreshToken(oauthRequest.getRefreshToken()));
			long creationTime = System.currentTimeMillis();
			tokenService.putToken(authorization.getRefreshToken(), accessToken,
					creationTime, creationTime + MongoCollections.ACCESS_TOKEN_EXPIRE_TIME);
		}
		Token token = new Token()
							.setRefreshToken(authorization.getRefreshToken())
							.setAccessToken(accessToken)
							.setUid(authorization.getUid())
							.setExpireIn(MongoCollections.ACCESS_TOKEN_EXPIRE_TIME / 1000);
		return token;
	}

	@Override
	public Validation validate(String accessToken, String authorization) {
		checkValidateBasicAuth(authorization);
		PojoToken pojoToken = checkGrantNotValid(tokenService.getTokenByAccessToken(accessToken));
		checkAccessTokenNotExpired(pojoToken);
		PojoAuthorization pojoAuthorization = checkGrantNotValid(authorizationService.getAuthorizationByRefreshToken(pojoToken.getRefreshToken()));
		Application application = checkClientNotValid(applicationService.getApplicationByAppId(pojoAuthorization.getAppId()));
		Account account = checkAccountNotNull(accountService.getAccountByUid(pojoAuthorization.getUid()));
		Validation validation = new Validation(account.getSndaId(),
				account.getLocale(), application.getAppid(),
				pojoAuthorization.getAuthorizedScope(), pojoToken.getAccessToken(),
				pojoToken.getExpire());
		return validation;
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
		if (accessorService.getAccessor(accessKey, secretKey) == null) {
			throw new InvalidAccessorException();
		}
	}
	
	private Account getOrCreateAccount(SdoValidation sdoValidation) {
		Account account = accountService.getAccountBySndaId(sdoValidation.getData().getSndaId());
		if (account == null) {
			long creationTime = System.currentTimeMillis();
			account = accountService.putAccount(sdoValidation.getData().getSndaId(), 
												ApplicationKeys.generateAccessKeyId(), 
												sdoValidation.getData().getInputUserId(), 
												sdoValidation.getData().getDisplayAccount(), 
												null, 
												Constants.DEFAULT_LOCALE, 
												true, 
												creationTime, 
												creationTime);
		}
		return account;
	}

}
