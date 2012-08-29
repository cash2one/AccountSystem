package com.snda.gcloud.as.mongo.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection=Collections.ACCOUNT_COLLECTION_NAME)
public class Account {

	@Id
	private String id;
	
	@Field(Collections.Account.SNDA_ID)
	private String sndaId;
	
	@Field(Collections.Account.UID)
	private String uid;
	
	@Field(Collections.Account.DISPLAY_NAME)
	private String displayName;
	
	@Field(Collections.Account.EMAIL)
	private String email;
	
	@Field(Collections.Account.LOCALE)
	private String locale;
	
	@Field(Collections.Account.CREATION_TIME)
	private long creationTime;
	
	@Field(Collections.Account.AVAILABLE)
	private boolean available;
	
	@PersistenceConstructor
	public Account(String sndaId, String uid,
			String displayName, String email,
			String locale, long creationTime,
			boolean available) {
		this.sndaId = sndaId;
		this.uid = uid;
		this.displayName = displayName;
		this.email = email;
		this.locale = locale;
		this.creationTime = creationTime;
		this.available = available;
	}
	
	public String getId() {
		return id;
	}

	public String getSndaId() {
		return sndaId;
	}

	public Account setSndaId(String sndaId) {
		this.sndaId = sndaId;
		return this;
	}

	public String getUid() {
		return uid;
	}

	public Account setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Account setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Account setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getLocale() {
		return locale;
	}

	public Account setLocale(String locale) {
		this.locale = locale;
		return this;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public Account setCreationTime(long creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public boolean isAvailable() {
		return available;
	}

	public Account setAvailable(boolean available) {
		this.available = available;
		return this;
	}
	
	@Override
	public String toString() {
		return "Account [id=" + id +
				", snda_id=" + sndaId +
				", uid=" + uid +
				", display_name=" + displayName +
				", email=" + email + 
				", locale=" + locale +
				", creation_time=" + creationTime +
				", available=" + available +
				"]";
	}
	
	public com.snda.gcloud.as.rest.model.Account getModelAccount() {
		com.snda.gcloud.as.rest.model.Account account = new com.snda.gcloud.as.rest.model.Account();
		account.setUid(getUid());
		account.setDisplay_name(getDisplayName());
		account.setEmail(getEmail());
		account.setLocale(getLocale());
		account.setCreationTime(new DateTime(getCreationTime()));
		account.setAvailable(isAvailable());
		return account;
	}
	
}
