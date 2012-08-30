package com.snda.grand.space.as.mongo.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.snda.grand.space.as.rest.model.Account;


@Document(collection=Collections.ACCOUNT_COLLECTION_NAME)
public class PojoAccount {

	@Id
	private String id;
	
	@Indexed(unique = true)
	@Field(Collections.Account.SNDA_ID)
	private String sndaId;
	
	@Indexed
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
	
	@Field(Collections.Account.MODIFIED_TIME)
	private long modifiedTime;
	
	@Indexed
	@Field(Collections.Account.AVAILABLE)
	private boolean available;
	
	@PersistenceConstructor
	public PojoAccount(String sndaId, String uid,
			String displayName, String email,
			String locale, long creationTime,
			long modifiedTime, boolean available) {
		this.sndaId = sndaId;
		this.uid = uid;
		this.displayName = displayName;
		this.email = email;
		this.locale = locale;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.available = available;
	}
	
	public String getId() {
		return id;
	}

	public String getSndaId() {
		return sndaId;
	}

	public PojoAccount setSndaId(String sndaId) {
		this.sndaId = sndaId;
		return this;
	}

	public String getUid() {
		return uid;
	}

	public PojoAccount setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public PojoAccount setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public PojoAccount setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getLocale() {
		return locale;
	}

	public PojoAccount setLocale(String locale) {
		this.locale = locale;
		return this;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public PojoAccount setCreationTime(long creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public long getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public boolean isAvailable() {
		return available;
	}

	public PojoAccount setAvailable(boolean available) {
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
	
	public Account getAccount() {
		Account account = new Account();
		account.setUid(getUid());
		account.setDisplay_name(getDisplayName());
		account.setEmail(getEmail());
		account.setLocale(getLocale());
		account.setCreationTime(new DateTime(getCreationTime()));
		account.setAvailable(isAvailable());
		return account;
	}
	
}
