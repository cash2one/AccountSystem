package com.snda.grand.space.as.rest.application;

import javax.ws.rs.core.Response;

import com.snda.grand.space.as.rest.model.Application;

public interface ApplicationResource {

	Application create(String appId, String uid, String appDescription,
			String appStatus, String scope, String website);
	
	Response listAuthorized(String appId, String owner);
	
	Response status(String appId, String owner);
	
	Response modify(String appId, String owner, String appDescription, String website);
	
	Response changeStatus(String appId, String owner, String appStatus);
	
	Response cancelAuthorization(String uid, String appId);
	
	Response delete(String appId, String owner);

}
