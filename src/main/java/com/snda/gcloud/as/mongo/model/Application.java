package com.snda.gcloud.as.mongo.model;

import static com.snda.gcloud.as.mongo.model.Collections.APPLICATION_COLLECTION_NAME;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;


@Document(collection = APPLICATION_COLLECTION_NAME)
public class Application {

	@Id
	private String id;
	
	@Field(Collections.Application.APPID)
	private String appid;
	
	@Field(Collections.Application.APP_DESCRIPTION)
	private String appDescription;
	
	@Field(Collections.Application.APP_STAUTS)
	private String appStatus;
	
	@Field(Collections.Application.APP_KEY)
	private String appKey;
	
	@Field(Collections.Application.APP_SECRET)
	private String appSecret;
	
	@Field(Collections.Application.SCOPE)
	private String scope;
	
	@Field(Collections.Application.WEBSITE)
	private String website;
	
	@Field(Collections.Application.CREATION_TIME)
	private long creationTime;
	
	@Field(Collections.Application.OWNER)
	private String owner;

	@PersistenceConstructor
	public Application(String appid, String appDescription, String appStatus,
			String appKey, String appSecret, String scope, String website,
			long creationTime, String owner) {
		this.appid = appid;
		this.appDescription = appDescription;
		this.appStatus = appStatus;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.scope = scope;
		this.website = website;
		this.creationTime = creationTime;
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public String getAppid() {
		return appid;
	}

	public Application setAppid(String appid) {
		this.appid = appid;
		return this;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public Application setAppDescription(String appDescription) {
		this.appDescription = appDescription;
		return this;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public Application setAppStatus(String appStatus) {
		this.appStatus = appStatus;
		return this;
	}

	public String getAppKey() {
		return appKey;
	}

	public Application setAppKey(String appKey) {
		this.appKey = appKey;
		return this;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public Application setAppSecret(String appSecret) {
		this.appSecret = appSecret;
		return this;
	}

	public String getScope() {
		return scope;
	}

	public Application setScope(String scope) {
		this.scope = scope;
		return this;
	}

	public String getWebsite() {
		return website;
	}

	public Application setWebsite(String website) {
		this.website = website;
		return this;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public Application setCreationTime(long creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public String getOwner() {
		return owner;
	}

	public Application setOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public com.snda.gcloud.as.rest.model.Application getModelApplication() {
		com.snda.gcloud.as.rest.model.Application application = new com.snda.gcloud.as.rest.model.Application();
		application.setAppid(appid);
		application.setAppDescription(appDescription);
		application.setAppStatus(appStatus);
		application.setAppKey(appKey);
		application.setAppSecret(appSecret);
		application.setScope(scope);
		application.setWebsite(website);
		application.setCreationTime(creationTime);
		application.setOwner(owner);
		return application;
	}
	
	public static List<com.snda.gcloud.as.rest.model.Application> getModelApplications(List<Application> apps) {
		List<com.snda.gcloud.as.rest.model.Application> modelApps = null;
		if (apps != null) {
			modelApps = Lists.newArrayList();
			for (Application app : apps) {
				modelApps.add(app.getModelApplication());
			}
		}
		return modelApps;
	}

}
