package com.snda.grand.mobile.as.exception;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;

public class ASOAuthAccessDeniedException extends ASOAuthProblemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1136920330050237045L;
	private static final String ERROR = "access_denied";
	private static final String ERROR_DESCRIPTION = "Access denied";

	public ASOAuthAccessDeniedException(OAuthProblemException oauthProblem,
			String api) {
		super(oauthProblem, api);
	}
	
	public static ASOAuthAccessDeniedException build(String api, String redirectUri) {
		OAuthProblemException oauthProblem = OAuthProblemException.error(ERROR, ERROR_DESCRIPTION);
		oauthProblem.setRedirectUri(redirectUri);
		return new ASOAuthAccessDeniedException(oauthProblem, api);
	}

}
