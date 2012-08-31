package com.snda.grand.space.as.mongo.model;

import static com.snda.grand.space.as.mongo.model.Collections.APPLICATION_COLLECTION_NAME;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;
import com.snda.grand.space.as.rest.model.Application;


@Document(collection = APPLICATION_COLLECTION_NAME)
public class PojoApplication {

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
	
	@Field(Collections.Application.MODIFIED_TIME)
	private long modifiedTime;
	
	@Field(Collections.Application.OWNER)
	private String owner;

	@PersistenceConstructor
	public PojoApplication(String appid, String appDescription, String appStatus,
			String appKey, String appSecret, String scope, String website,
			long creationTime, long modifiedTime, String owner) {
		this.appid = appid;
		this.appDescription = appDescription;
		this.appStatus = appStatus;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.scope = scope;
		this.website = website;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public String getAppid() {
		return appid;
	}

	public PojoApplication setAppid(String appid) {
		this.appid = appid;
		return this;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public PojoApplication setAppDescription(String appDescription) {
		this.appDescription = appDescription;
		return this;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public PojoApplication setAppStatus(String appStatus) {
		this.appStatus = appStatus;
		return this;
	}

	public String getAppKey() {
		return appKey;
	}

	public PojoApplication setAppKey(String appKey) {
		this.appKey = appKey;
		return this;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public PojoApplication setAppSecret(String appSecret) {
		this.appSecret = appSecret;
		return this;
	}

	public String getScope() {
		return scope;
	}

	public PojoApplication setScope(String scope) {
		this.scope = scope;
		return this;
	}

	public String getWebsite() {
		return website;
	}

	public PojoApplication setWebsite(String website) {
		this.website = website;
		return this;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public PojoApplication setCreationTime(long creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public long getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getOwner() {
		return owner;
	}

	public PojoApplication setOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public Application getApplication() {
		Application application = new Application();
		application.setAppid(appid);
		application.setAppDescription(appDescription);
		application.setAppStatus(appStatus);
		application.setAppKey(appKey);
		application.setAppSecret(appSecret);
		application.setScope(scope);
		application.setWebsite(website);
		application.setCreationTime(new DateTime(creationTime));
		application.setModifiedTime(new DateTime(modifiedTime));
		application.setOwner(owner);
		return application;
	}
	
	public static List<Application> getApplications(List<PojoApplication> pojoApps) {
		List<Application> apps = null;
		if (pojoApps != null) {
			apps = Lists.newArrayList();
			for (PojoApplication pojoApp : pojoApps) {
				apps.add(pojoApp.getApplication());
			}
		}
		return apps;
	}

}
