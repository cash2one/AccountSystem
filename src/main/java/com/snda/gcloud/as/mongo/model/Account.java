package com.snda.gcloud.as.mongo.model;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;


@Document(collection = "account")
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

	public void setId(String id) {
		this.id = id;
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

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
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
