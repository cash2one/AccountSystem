package com.snda.gcloud.as.mongo.model;

import static com.snda.gcloud.as.mongo.model.Collections.TOKEN_COLLECTION_NAME;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;


@Document(collection = TOKEN_COLLECTION_NAME)
public class Token {
	
	@Id
	private String id;
	private String uid;
	private String appId;
	private String token;
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

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
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
