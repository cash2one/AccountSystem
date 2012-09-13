package com.snda.grand.space.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Validation {

	@JsonProperty("username")
	private String username;
	
	@JsonProperty("locale")
	private String locale;
	
	@JsonProperty("appid")
	private String appId;
	
	@JsonProperty("scope")
	private String scope;
	
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("expire")
	private long expire;
	
	public Validation(String username, String locale, String appId, String scope,
			String accessToken, long expire) {
		this.username = username;
		this.locale = locale;
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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
}
