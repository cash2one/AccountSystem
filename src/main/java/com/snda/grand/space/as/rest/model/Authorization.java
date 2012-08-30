package com.snda.grand.space.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Authorization {

	private String uid;
	private String appId;
	private String refreshToken;
	private DateTime authorizedTime;
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public DateTime getAuthorizedTime() {
		return authorizedTime;
	}

	public void setAuthorizedTime(DateTime authorizedTime) {
		this.authorizedTime = authorizedTime;
	}

}
