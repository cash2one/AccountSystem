package com.snda.gcloud.as.rest.application;

import javax.ws.rs.core.Response;

public interface ApplicationResource {

	Response create(String appId, String uid, String appDescription,
			String appStatus, String scope, String website);
	
	Response listAuthorized(String appId, String owner);
	
	Response status(String appId, String owner);
	
	Response modify(String appId, String uid, String appDescription, String website);
	
	Response changeStatus(String appId, String uid, String appStatus);
	
	Response cancelAuthorization(String uid, String appId);
	
	Response delete(String appId, String uid);

}
