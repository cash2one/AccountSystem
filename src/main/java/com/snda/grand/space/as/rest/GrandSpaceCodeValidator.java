package com.snda.grand.space.as.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.validators.AbstractValidator;

public class GrandSpaceCodeValidator extends
		AbstractValidator<HttpServletRequest> {

	public GrandSpaceCodeValidator() {
        requiredParams.add(OAuth.OAUTH_RESPONSE_TYPE);
        requiredParams.add(OAuth.OAUTH_CLIENT_ID);
//        optionalParams.put(OAuth.OAUTH_SCOPE, Constants.SCOPES);
    }

    @Override
    public void validateMethod(HttpServletRequest request) throws OAuthProblemException {
        String method = request.getMethod();
        if (!OAuth.HttpMethod.GET.equals(method) && !OAuth.HttpMethod.POST.equals(method)) {
            throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_REQUEST)
                .description("Method not correct.");
        }
    }

    @Override
    public void validateContentType(HttpServletRequest request) throws OAuthProblemException {
    }

}
