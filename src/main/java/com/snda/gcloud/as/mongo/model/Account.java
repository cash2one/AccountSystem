package com.snda.gcloud.as.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="accounts")
public class Account {

	@Id
	private String id;
	private String sndaId;
	private String uid;
	private String displayName;
	private String email;
	private String locale;
	private long creationTime;
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

	public void setSndaId(String sndaId) {
		this.sndaId = sndaId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
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
	
}
