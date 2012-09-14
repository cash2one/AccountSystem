package com.snda.grand.space.as.mongo.internal.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.snda.grand.space.as.mongo.model.MongoCollections;


@JsonIgnoreProperties(ignoreUnknown=true)
@Document(collection=MongoCollections.ACCESSOR_COLLECTION_NAME)
public class Accessor {
	
	@Id
	private String id;

	@Field(MongoCollections.Accessor.USERNAME)
	private String username;
	
	@Field(MongoCollections.Accessor.ACCESS_KEY)
	private String accessKey;
	
	@Field(MongoCollections.Accessor.SECRET_KEY)
	private String secretKey;
	
	@Field(MongoCollections.Accessor.DESCRIPTION)
	private String description;
	
	@Field(MongoCollections.Accessor.CREATION_TIME)
	private long creationTime;
	
	@PersistenceConstructor
	public Accessor(String username, String accessKey, String secretKey, String description) {
		this.username = username;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.description = description;
		this.creationTime = System.currentTimeMillis();
	}
	
	@JsonCreator
	public Accessor(@JsonProperty("id") String id, 
					@JsonProperty("username") String username, 
					@JsonProperty("accessKey") String accessKey, 
					@JsonProperty("secretKey") String secretKey, 
					@JsonProperty("description") String description, 
					@JsonProperty("creationTime") long creationTime) {
		this.id = id;
		this.username = username;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.description = description;
		this.creationTime = creationTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
}
