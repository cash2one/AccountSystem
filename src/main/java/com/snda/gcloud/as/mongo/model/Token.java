package com.snda.gcloud.as.mongo.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Maps;


@Document(collection = Collections.TOKEN_COLLECTION_NAME)
public class Token {

	@Id
	private String id;
	
	@Indexed(unique = true)
	@Field(Collections.Token.REFRESH_TOKEN)
	private String refreshToken;
	
	@DBRef
	@Field(Collections.Token.ACCESS_TOKEN)
	private Map<String, AccessToken> accessTokens;
	
	@PersistenceConstructor
	public Token(String refreshToken, Map<String, AccessToken> accessTokens) {
		this.refreshToken = refreshToken;
		this.accessTokens = Maps.newHashMap(accessTokens);
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

	public Map<String, AccessToken> getAccessTokens() {
		return accessTokens;
	}

	public void setAccessTokens(Map<String, AccessToken> accessTokens) {
		this.accessTokens = accessTokens;
	}
	
	public void addAccessToken(AccessToken accessToken) {
		if (!accessTokens.containsKey(accessToken)) {
			accessTokens.put(accessToken.getToken(), accessToken);
		}
	}
	
	public void removeAccessToken(AccessToken accessToken) {
		this.removeAccessToken(accessToken.getToken());
	}
	
	public void removeAccessToken(String accessToken) {
		if (accessTokens.containsKey(accessToken)) {
			accessTokens.remove(accessToken);
		}
	}
	
}
