package com.snda.gcloud.as.rest.impl;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.snda.gcloud.as.rest.OAuth2Resource;

@Service
@Path("/")
public class OAuth2ResourceImpl implements OAuth2Resource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ResourceImpl.class);
	
	public OAuth2ResourceImpl() {
		LOGGER.info("OAuth2ResourceImpl initialized.");
	}

	@Override
	@POST
	@Path("authorize")
	public Response authorize(@PathParam("client_id") String clientId,
			@PathParam("response_type") String responseType,
			@PathParam("redirect_uri") String redirectUri,
			@PathParam("scope") String scope) {
		// TODO Auto-generated method stub
		LOGGER.info("authorize client_id:<" + clientId + 
					"> response_type:<" + responseType +
					"> redirect_uri:<" + redirectUri + 
					"> scope:<" + scope + ">");
		return Response.ok().build();
	}

	@Override
	@GET
	@Path("access_token")
	public Response exchangeAccessToken(@PathParam("client_id") String clientId,
			@PathParam("client_secret") String clientSecret,
			@PathParam("grant_type") String grantType,
			@PathParam("redirect_uri") String redirectUri,
			@PathParam("code") String code) {
		// TODO Auto-generated method stub
		LOGGER.info("access_token client_id:<" + clientId +
					"> grant_type:<" + grantType +
					"> redirect_uri:<" + redirectUri + 
					"> code:<" + code + ">");
		return Response.ok().build();
	}
	
}
