package com.snda.grand.mobile.as.exception;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;

public class ASOAuthDomainMismatchException extends ASOAuthProblemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3084961905354481900L;
	private static final String ERROR = "domain_mismatch";
	private static final String ERROR_DESCRIPTION = "Domain mismatch";

	public ASOAuthDomainMismatchException(OAuthProblemException oauthProblem,
			String api) {
		super(oauthProblem, api);
	}
	
	public static ASOAuthDomainMismatchException build(String api, String redirectUri) {
		OAuthProblemException oauthProblem = OAuthProblemException.error(ERROR, ERROR_DESCRIPTION);
		oauthProblem.setRedirectUri(redirectUri);
		return new ASOAuthDomainMismatchException(oauthProblem, api);
	}

}
