package com.snda.grand.space.as.rest.application;

import java.util.List;

import com.snda.grand.space.as.rest.model.Application;
import com.snda.grand.space.as.rest.model.Authorization;

public interface ApplicationResource {

	Application create(String appId, String uid, String appDescription,
			String appStatus, String scope, String website);
	
	List<Authorization> listAuthorized(String appId, String owner);
	
	Application status(String appId, String owner);
	
	Application modify(String appId, String owner, String appDescription, String website);
	
	Application changeStatus(String appId, String owner, String appStatus);
	
	void cancelAuthorization(String uid, String appId);
	
	void delete(String appId, String owner);

}
