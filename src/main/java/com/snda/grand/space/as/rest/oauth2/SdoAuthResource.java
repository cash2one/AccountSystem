package com.snda.grand.space.as.rest.oauth2;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public interface SdoAuthResource {

	Response sdoAuthorize(HttpServletRequest request)
			throws URISyntaxException, OAuthProblemException, OAuthSystemException, IOException;
	
}
