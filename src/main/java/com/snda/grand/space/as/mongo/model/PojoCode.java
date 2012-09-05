package com.snda.grand.space.as.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = Collections.CODE_COLLECTION_NAME)
public class PojoCode {

	@Id
	private String id;
	
	@Indexed(unique = true)
	@Field(Collections.Code.CODE)
	private String code;
	
	@Indexed
	@Field(Collections.Code.UID)
	private String uid;
	
	@Indexed
	@Field(Collections.Code.APPID)
	private String appId;
	
	@Field(Collections.Code.CREATION_TIME)
	private long creationTime;
	
	@PersistenceConstructor
	public PojoCode(String code, String uid, String appId, long creationTime) {
		this.code = code;
		this.uid = uid;
		this.appId = appId;
		this.creationTime = creationTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	
	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

}
