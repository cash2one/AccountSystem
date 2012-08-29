package com.snda.gcloud.as.mongo.model;

public class AccessToken {
	
	private String token;
	private long creation_time;
	private long expire;
	
	public AccessToken(String token, long creation_time, long expire) {
		this.token = token;
		this.creation_time = creation_time;
		this.expire = expire;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(long creation_time) {
		this.creation_time = creation_time;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

}
