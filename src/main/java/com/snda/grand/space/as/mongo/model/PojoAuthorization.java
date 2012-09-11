package com.snda.grand.space.as.mongo.model;

import static com.snda.grand.space.as.mongo.model.MongoCollections.AUTHORIZATION_COLLECTION_NAME;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;
import com.snda.grand.space.as.rest.model.Authorization;


@Document(collection = AUTHORIZATION_COLLECTION_NAME)
@CompoundIndexes({
	@CompoundIndex(name = "authorization_index", def = "{" +
			MongoCollections.Authorization.UID + " : 1, " +
			MongoCollections.Authorization.APPID + " : 1, " +
			MongoCollections.Authorization.REFRESH_TOKEN + " : 1}")
})
public class PojoAuthorization {
	
	@Id
	private String id;
	
	@Indexed
	@Field(MongoCollections.Authorization.UID)
	private String uid;
	
	@Indexed
	@Field(MongoCollections.Authorization.APPID)
	private String appId;
	
	@Indexed(unique = true)
	@Field(MongoCollections.Authorization.REFRESH_TOKEN)
	private String refreshToken;
	
	@Field(MongoCollections.Authorization.AUTHORIZED_TIME)
	private long authorizedTime;
	
	@PersistenceConstructor
	public PojoAuthorization(String uid, String appId, String refreshToken, long authorizedTime) {
		this.uid = uid;
		this.appId = appId;
		this.refreshToken = refreshToken;
		this.authorizedTime = authorizedTime;
	}
	
	public String getId() {
		return id;
	}

	public String getUid() {
		return uid;
	}

	public PojoAuthorization setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public String getAppId() {
		return appId;
	}

	public PojoAuthorization setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public PojoAuthorization setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}
	
	public long getAuthorizedTime() {
		return authorizedTime;
	}

	public PojoAuthorization setAuthorizedTime(long authorizedTime) {
		this.authorizedTime = authorizedTime;
		return this;
	}

	@Override
	public String toString() {
		return "Authorization [id=" + id +
				", uid=" + uid +
				", appId=" + appId +
				", token=" + refreshToken +
				", authorizedTime=" + authorizedTime +
				"]";
	}

	public Authorization getAuthorization(String publisherName, String scope) {
		Authorization authorization = new Authorization();
		authorization.setUid(uid);
		authorization.setAppId(appId);
		authorization.setRefreshToken(this.refreshToken);
		authorization.setAuthorizedTime(new DateTime(authorizedTime));
		authorization.setPublisherName(publisherName);
		authorization.setScope(scope);
		return authorization;
	}
	
	public static List<Authorization> getAuthorizations(
			List<PojoAuthorization> pojoAuthorizations, String publisherName,
			String scope) {
		List<Authorization> authorizations = null;
		if (pojoAuthorizations != null) {
			authorizations = Lists.newArrayList();
			for (PojoAuthorization pojoAuthorization : pojoAuthorizations) {
				authorizations.add(pojoAuthorization.getAuthorization(publisherName, scope));
			}
		}
		return authorizations;
	}

}
