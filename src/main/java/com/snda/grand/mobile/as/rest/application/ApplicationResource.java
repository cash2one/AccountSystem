package com.snda.grand.mobile.as.rest.application;

import java.util.List;

import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.rest.model.Authorization;

public interface ApplicationResource {

	Application create(String appId, String uid, String appDescription,
			String appStatus, String publisherName, String scope, String website);
	
	List<Authorization> listAuthorized(String appId, String owner);
	
	Application status(String appId, String owner);
	
	Application modify(String appId, String modifiedAppId, String owner, String appDescription, String website, String publisherName);
	
	Application changeStatus(String appId, String owner, String appStatus);
	
	void cancelAuthorization(String uid, String appId);
	
	void delete(String appId, String owner);

}
