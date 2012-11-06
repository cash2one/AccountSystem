package com.snda.grand.mobile.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Application {
	
	private String appid;
	private String appDescription;
	private String appStatus;
	private String appKey;
	private String appSecret;
	private String publisherName;
	private String scope;
	private String website;
	private DateTime creationTime;
	private DateTime modifiedTime;
	private String owner;
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}
	
	public DateTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(DateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

}
