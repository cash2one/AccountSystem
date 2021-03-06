package com.snda.grand.mobile.as.rest.oauth2;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.snda.grand.mobile.as.exception.ASOAuthProblemException;

public interface SdoAuthResource {

	Response sdoAuthorize(HttpServletRequest request)
			throws URISyntaxException, ASOAuthProblemException, OAuthSystemException, IOException;
	
	Response denyAuthorize(HttpServletRequest resquest)
			throws ASOAuthProblemException;
	
}
