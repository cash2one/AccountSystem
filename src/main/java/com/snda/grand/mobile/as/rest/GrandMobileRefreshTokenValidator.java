package com.snda.grand.mobile.as.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.validators.AbstractValidator;

public class GrandMobileRefreshTokenValidator extends
		AbstractValidator<HttpServletRequest> {

	public GrandMobileRefreshTokenValidator() {
        requiredParams.add(OAuth.OAUTH_GRANT_TYPE);
        requiredParams.add(OAuth.OAUTH_CLIENT_ID);
        requiredParams.add(OAuth.OAUTH_REFRESH_TOKEN);
    }
	
}
