package com.snda.gcloud.as.mongo.model;

import static com.snda.gcloud.as.mongo.model.Collections.TOKEN_COLLECTION_NAME;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;


@Document(collection = TOKEN_COLLECTION_NAME)
public class Token {
	
	@Id
	private String id;
	
	@Field(Collections.Token.UID)
	private String uid;
	
	@Field(Collections.Token.APPID)
	private String appId;
	
	@Field(Collections.Token.TOKEN)
	private String token;
	
	@Field(Collections.Token.EXPIRE)
	private long expire;
	
	@PersistenceConstructor
	public Token(String uid, String appId, String token, long expire) {
		this.uid = uid;
		this.appId = appId;
		this.token = token;
		this.expire = expire;
	}
	
	public String getId() {
		return id;
	}

	public String getUid() {
		return uid;
	}

	public Token setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public String getAppId() {
		return appId;
	}

	public Token setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getToken() {
		return token;
	}

	public Token setToken(String token) {
		this.token = token;
		return this;
	}

	public long getExpire() {
		return expire;
	}

	public Token setExpire(long expire) {
		this.expire = expire;
		return this;
	}
	
	@Override
	public String toString() {
		return "Token [id=" + id +
				", uid=" + uid +
				", appId=" + appId +
				", token=" + token +
				", expire=" + expire +
				"]";
	}

	public com.snda.gcloud.as.rest.model.Token getModelToken() {
		com.snda.gcloud.as.rest.model.Token token = new com.snda.gcloud.as.rest.model.Token();
		token.setUid(uid);
		token.setAppId(appId);
		token.setToken(this.token);
		token.setExpire(expire);
		return token;
	}
	
	public static List<com.snda.gcloud.as.rest.model.Token> getModelTokens(List<Token> tokens) {
		List<com.snda.gcloud.as.rest.model.Token> modelTokens = null;
		if (tokens != null) {
			modelTokens = Lists.newArrayList();
			for (Token token : tokens) {
				modelTokens.add(token.getModelToken());
			}
		}
		return modelTokens;
	}
	
}
