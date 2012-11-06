package com.snda.grand.mobile.as.rest.authorization.impl;

import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAppid;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkUid;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Service;

import com.snda.grand.mobile.as.processor.AuthorizationResourceProcessor;
import com.snda.grand.mobile.as.rest.authorization.AuthorizationResource;
import com.snda.grand.mobile.as.rest.model.Authorization;


@Service
@Path("authorization")
public class AuthorizationResourceImpl implements AuthorizationResource {
	
	private final AuthorizationResourceProcessor authorizationResourceProcessor;
	
	public AuthorizationResourceImpl(AuthorizationResourceProcessor authorizationResourceProcessor) {
		this.authorizationResourceProcessor = authorizationResourceProcessor;
	}

	@Override
	@GET
	public Authorization getAuthorization(@QueryParam("uid") String uid, 
			@QueryParam("appid") String appId) {
		checkUid(uid);
		checkAppid(appId);
		return authorizationResourceProcessor.getAuthorizationByUidAndAppId(uid, appId);
	}

}
