package com.snda.grand.space.as.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.validators.AbstractValidator;

public class GrandSpaceRefreshTokenValidator extends
		AbstractValidator<HttpServletRequest> {

	public GrandSpaceRefreshTokenValidator() {
        requiredParams.add(OAuth.OAUTH_GRANT_TYPE);
        requiredParams.add(OAuth.OAUTH_CLIENT_ID);
        requiredParams.add(OAuth.OAUTH_REFRESH_TOKEN);
    }
	
}
