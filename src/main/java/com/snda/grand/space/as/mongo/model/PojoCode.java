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
	
	@Field(Collections.Code.CODE)
	@Indexed
	private String code;
	
	@Field(Collections.Code.CREATION_TIME)
	private long creationTime;
	
	@PersistenceConstructor
	public PojoCode(String code, long creationTime) {
		this.code = code;
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

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
}
