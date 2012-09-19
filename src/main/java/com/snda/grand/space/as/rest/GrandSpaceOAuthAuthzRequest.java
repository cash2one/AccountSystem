package com.snda.grand.space.as.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.as.request.OAuthRequest;
import org.apache.amber.oauth2.as.validator.TokenValidator;
import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.apache.amber.oauth2.common.utils.OAuthUtils;
import org.apache.amber.oauth2.common.validators.OAuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrandSpaceOAuthAuthzRequest extends OAuthRequest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GrandSpaceOAuthAuthzRequest.class);
	
	public GrandSpaceOAuthAuthzRequest(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        super(request);
    }
	
	@Override
	protected void validate() throws OAuthSystemException, OAuthProblemException {
        try {
            validator = initValidator();
            validator.validateMethod(request);
            validator.validateContentType(request);
            validator.validateRequiredParameters(request);
            validator.validateOptionalParameters(request);
        } catch (OAuthProblemException e) {
            try {
                String redirectUri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
                if (!OAuthUtils.isEmpty(redirectUri)) {
                    e.setRedirectUri(redirectUri);
                }
            } catch (Exception ex) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Cannot read redirect_url from the request: {}", new String[] {ex.getMessage()});
                }
            }

            throw e;
        }

    }

	@Override
    protected OAuthValidator<HttpServletRequest> initValidator() throws OAuthProblemException, OAuthSystemException {
        //end user authorization validators
        validators.put(ResponseType.CODE.toString(), GrandSpaceCodeValidator.class);
        validators.put(ResponseType.TOKEN.toString(), TokenValidator.class);
        
        String requestTypeValue = getParam(OAuth.OAUTH_RESPONSE_TYPE);
        if (OAuthUtils.isEmpty(requestTypeValue)) {
            throw OAuthUtils.handleOAuthProblemException("Missing response_type parameter value");
        }
        Class<? extends OAuthValidator<HttpServletRequest>> clazz = validators.get(requestTypeValue);
        if (clazz == null) {
            throw OAuthUtils.handleOAuthProblemException("Invalid response_type parameter value");
        }
        return OAuthUtils.instantiateClass(clazz);

    }

    public String getState() {
        return getParam(OAuth.OAUTH_STATE);
    }

    public String getResponseType() {
        return getParam(OAuth.OAUTH_RESPONSE_TYPE);
    }

}
