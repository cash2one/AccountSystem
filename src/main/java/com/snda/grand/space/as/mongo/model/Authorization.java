package com.snda.grand.space.as.mongo.model;

import static com.snda.grand.space.as.mongo.model.Collections.AUTHORIZATION_COLLECTION_NAME;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;


@Document(collection = AUTHORIZATION_COLLECTION_NAME)
public class Authorization {
	
	@Id
	private String id;
	
	@Field(Collections.Authorization.UID)
	private String uid;
	
	@Field(Collections.Authorization.APPID)
	private String appId;
	
	@Field(Collections.Authorization.REFRESH_TOKEN)
	private String refreshToken;
	
	@Field(Collections.Authorization.EXPIRE)
	private long expire;
	
	@PersistenceConstructor
	public Authorization(String uid, String appId, String refreshToken, long expire) {
		this.uid = uid;
		this.appId = appId;
		this.refreshToken = refreshToken;
		this.expire = expire;
	}
	
	public String getId() {
		return id;
	}

	public String getUid() {
		return uid;
	}

	public Authorization setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public String getAppId() {
		return appId;
	}

	public Authorization setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Authorization setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	public long getExpire() {
		return expire;
	}

	public Authorization setExpire(long expire) {
		this.expire = expire;
		return this;
	}
	
	@Override
	public String toString() {
		return "Authorization [id=" + id +
				", uid=" + uid +
				", appId=" + appId +
				", token=" + refreshToken +
				", expire=" + expire +
				"]";
	}

	public com.snda.grand.space.as.rest.model.Authorization getModelAuthorization() {
		com.snda.grand.space.as.rest.model.Authorization authorization = new com.snda.grand.space.as.rest.model.Authorization();
		authorization.setUid(uid);
		authorization.setAppId(appId);
		authorization.setRefreshToken(this.refreshToken);
		authorization.setExpire(expire);
		return authorization;
	}
	
	public static List<com.snda.grand.space.as.rest.model.Authorization> getModelAuthorizations(List<Authorization> authorizations) {
		List<com.snda.grand.space.as.rest.model.Authorization> modelAuthorizations = null;
		if (authorizations != null) {
			modelAuthorizations = Lists.newArrayList();
			for (Authorization authorization : authorizations) {
				modelAuthorizations.add(authorization.getModelAuthorization());
			}
		}
		return modelAuthorizations;
	}
	
}
