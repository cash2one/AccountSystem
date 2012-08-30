package com.snda.grand.space.as.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = Collections.TOKEN_COLLECTION_NAME)
public class PojoToken {

	@Id
	private String id;
	
	@Field(Collections.Token.REFRESH_TOKEN)
	private String refreshToken;
	
	@Field(Collections.Token.ACCESS_TOKEN)
	private String accessToken;
	
	@Field(Collections.Token.CREATION_TIME)
	private long creationTime;
	
	@Field(Collections.Token.EXPIRE)
	private long expire;
	
	@PersistenceConstructor
	public PojoToken(String refreshToken, String accessToken, long creationTime, long expire) {
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
		this.creationTime = creationTime;
		this.expire = expire;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}
	
}
