package com.snda.grand.mobile.as.mongo.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.snda.grand.mobile.as.rest.model.Account;


@Document(collection=MongoCollections.ACCOUNT_COLLECTION_NAME)
public class PojoAccount {

	@Id
	private String id;
	
	@Indexed(unique = true)
	@Field(MongoCollections.Account.SNDA_ID)
	private String sndaId;
	
	@Indexed(unique = true)
	@Field(MongoCollections.Account.UID)
	private String uid;
	
	@Field(MongoCollections.Account.USERNAME_NORM)
	private String usernameNorm;
	
	@Field(MongoCollections.Account.DISPLAY_NAME)
	private String displayName;
	
	@Field(MongoCollections.Account.EMAIL)
	private String email;
	
	@Field(MongoCollections.Account.LOCALE)
	private String locale;
	
	@Field(MongoCollections.Account.CREATION_TIME)
	private long creationTime;
	
	@Field(MongoCollections.Account.MODIFIED_TIME)
	private long modifiedTime;
	
	@Indexed
	@Field(MongoCollections.Account.AVAILABLE)
	private boolean available;
	
	@PersistenceConstructor
	public PojoAccount(String sndaId, String uid, String usernameNorm,
			String displayName, String email, String locale, long creationTime,
			long modifiedTime, boolean available) {
		this.sndaId = sndaId;
		this.uid = uid;
		this.usernameNorm = usernameNorm;
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

	public String getUsernameNorm() {
		return usernameNorm;
	}

	public PojoAccount setUsernameNorm(String usernameNorm) {
		this.usernameNorm = usernameNorm;
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
				", username_norm=" + usernameNorm +
				", display_name=" + displayName +
				", email=" + email + 
				", locale=" + locale +
				", creation_time=" + creationTime +
				", modified_time=" + modifiedTime +
				", available=" + available +
				"]";
	}
	
	public Account getAccount() {
		Account account = new Account();
		account.setSndaId(sndaId);
		account.setUid(uid);
		account.setUsernameNorm(usernameNorm);
		account.setDisplayName(displayName);
		account.setEmail(email);
		account.setLocale(locale);
		account.setCreationTime(new DateTime(creationTime));
		account.setModifiedTime(new DateTime(modifiedTime));
		account.setAvailable(available);
		return account;
	}
	
}
