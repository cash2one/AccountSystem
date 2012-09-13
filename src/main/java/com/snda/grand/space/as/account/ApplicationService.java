package com.snda.grand.space.as.account;

import java.util.List;

import com.snda.grand.space.as.rest.model.Application;

public interface ApplicationService {

	Application putApplication(String appId, String uid, String appKey, 
			String appSecret, String appDescription,
			String appStatus, String publisherName, String scope,
			String website, long creationTime, long modifiedTime);
	
	Application updateApplication(String appId, String uid, String appKey,
			String appSecret, String appDescription, String appStatus,
			String publisherName, String scope, String website,
			long creationTime, long modifiedTime);
	
	Application getApplicationByAppId(String appId);
	
	List<Application> listApplications(String owner);
	
	void deleteApplication(String appId);
	
}
