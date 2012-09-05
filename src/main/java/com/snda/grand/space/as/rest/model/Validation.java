package com.snda.grand.space.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Validation {

	private String username;
	
	private String appId;
	
	private String scope;
	
	private String accessToken;
	
	private long expire;
	
	public Validation(String username, String appId, String scope,
			String accessToken, long expire) {
		this.username = username;
		this.appId = appId;
		this.scope = scope;
		this.accessToken = accessToken;
		this.expire = expire;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}
	
}
