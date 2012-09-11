package com.snda.grand.space.as.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.as.request.OAuthRequest;
import org.apache.amber.oauth2.as.validator.ClientCredentialValidator;
import org.apache.amber.oauth2.as.validator.PasswordValidator;
import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.amber.oauth2.common.utils.OAuthUtils;
import org.apache.amber.oauth2.common.validators.OAuthValidator;

public class GrandSpaceOAuthTokenRequest extends OAuthRequest {
	
	public GrandSpaceOAuthTokenRequest(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        super(request);
    }

    @Override
    protected OAuthValidator<HttpServletRequest> initValidator() throws OAuthProblemException, OAuthSystemException {
        validators.put(GrantType.PASSWORD.toString(), PasswordValidator.class);
        validators.put(GrantType.CLIENT_CREDENTIALS.toString(), ClientCredentialValidator.class);
        validators.put(GrantType.AUTHORIZATION_CODE.toString(), GrandSpaceAuthorizationCodeValidator.class);
        validators.put(GrantType.REFRESH_TOKEN.toString(), GrandSpaceRefreshTokenValidator.class);
        String requestTypeValue = getParam(OAuth.OAUTH_GRANT_TYPE);
        if (OAuthUtils.isEmpty(requestTypeValue)) {
            throw OAuthUtils.handleOAuthProblemException("Missing grant_type parameter value");
        }
        Class<? extends OAuthValidator<HttpServletRequest>> clazz = validators.get(requestTypeValue);
        if (clazz == null) {
            throw OAuthUtils.handleOAuthProblemException("Invalid grant_type parameter value");
        }
        return OAuthUtils.instantiateClass(clazz);
    }

	public String getPassword() {
        return getParam(OAuth.OAUTH_PASSWORD);
    }

    public String getUsername() {
        return getParam(OAuth.OAUTH_USERNAME);
    }

    public String getRefreshToken() {
        return getParam(OAuth.OAUTH_REFRESH_TOKEN);
    }
    
    public String getCode() {
        return getParam(OAuth.OAUTH_CODE);
    }

    public String getGrantType() {
        return getParam(OAuth.OAUTH_GRANT_TYPE);
    }

}
