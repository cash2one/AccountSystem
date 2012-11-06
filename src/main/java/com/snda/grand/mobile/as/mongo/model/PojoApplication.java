package com.snda.grand.mobile.as.mongo.model;

import static com.snda.grand.mobile.as.mongo.model.MongoCollections.APPLICATION_COLLECTION_NAME;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;
import com.snda.grand.mobile.as.rest.model.Application;


@Document(collection = APPLICATION_COLLECTION_NAME)
public class PojoApplication {

	@Id
	private String id;
	
	@Indexed(unique = true)
	@Field(MongoCollections.Application.APPID)
	private String appid;
	
	@Field(MongoCollections.Application.APP_DESCRIPTION)
	private String appDescription;
	
	@Field(MongoCollections.Application.APP_STAUTS)
	private String appStatus;
	
	@Field(MongoCollections.Application.APP_KEY)
	private String appKey;
	
	@Field(MongoCollections.Application.APP_SECRET)
	private String appSecret;
	
	@Field(MongoCollections.Application.PUBLISHER_NAME)
	private String publisherName;
	
	@Field(MongoCollections.Application.SCOPE)
	private String scope;
	
	@Field(MongoCollections.Application.WEBSITE)
	private String website;
	
	@Field(MongoCollections.Application.CREATION_TIME)
	private long creationTime;
	
	@Field(MongoCollections.Application.MODIFIED_TIME)
	private long modifiedTime;
	
	@Indexed
	@Field(MongoCollections.Application.OWNER)
	private String owner;

	@PersistenceConstructor
	public PojoApplication(String appid, String appDescription, String appStatus,
			String appKey, String appSecret, String publisherName, String scope, String website,
			long creationTime, long modifiedTime, String owner) {
		this.appid = appid;
		this.appDescription = appDescription;
		this.appStatus = appStatus;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.publisherName = publisherName;
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
	
	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
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
		application.setPublisherName(publisherName);
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
	
	@Override
	public String toString() {
		return "Application [id=" + id +
				", appid=" + appid +
				", appDescription=" + appDescription +
				", appStatus=" + appStatus +
				", appKey=" + appKey +
				", appSecret=" + appSecret + 
				", publisherName=" + publisherName +
				", scope=" + scope +
				", website=" + website +
				", creation_time=" + creationTime +
				", modified_time=" + modifiedTime +
				", owner=" + owner +
				"]";
	}

}
