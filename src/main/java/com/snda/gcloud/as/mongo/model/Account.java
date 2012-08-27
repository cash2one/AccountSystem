package com.snda.gcloud.as.mongo.model;

import static com.snda.gcloud.as.rest.util.Constants.ACCOUNT_COLLECTION_NAME;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;


@Document(collection = ACCOUNT_COLLECTION_NAME)
public class Account {

	@Id
	private String id;
	private String sndaId;
	private String uid;
	private String displayName;
	private String email;
	private String locale;
	private long creationTime;
	private List<Device> devices;
	private boolean available;
	
	@PersistenceConstructor
	public Account(String sndaId, String uid,
			String displayName, String email,
			String locale, long creationTime,
			List<Device> devices, boolean available) {
		this.sndaId = sndaId;
		this.uid = uid;
		this.displayName = displayName;
		this.email = email;
		this.locale = locale;
		this.creationTime = creationTime;
		this.devices = Lists.newArrayList(devices);
		this.available = true;
	}

	public String getId() {
		return id;
	}

	public Account setId(String id) {
		this.id = id;
		return this;
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

	public List<Device> getDevices() {
		return devices;
	}

	public Account setDevices(List<Device> devices) {
		this.devices = devices;
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
				", devices=" + devices +
				", available=" + available +
				"]";
	}
	
	public com.snda.gcloud.as.rest.model.Account getModelAccount() {
		com.snda.gcloud.as.rest.model.Account account = new com.snda.gcloud.as.rest.model.Account();
		account.setUid(uid);
		account.setDisplay_name(displayName);
		account.setEmail(email);
		account.setLocale(locale);
		account.setCreationTime(new DateTime(creationTime));
		account.setAvailable(available);
		return account;
	}
	
}
