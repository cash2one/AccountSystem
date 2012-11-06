package com.snda.grand.mobile.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Token {
	
	@JsonProperty("uid")
	private String uid;
	
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("expire_in")
	private long expireIn;
	
	public String getUid() {
		return uid;
	}

	public Token setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public Token setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public long getExpireIn() {
		return expireIn;
	}

	public Token setExpireIn(long expireIn) {
		this.expireIn = expireIn;
		return this;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Token setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

}
