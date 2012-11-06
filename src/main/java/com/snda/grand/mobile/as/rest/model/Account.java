package com.snda.grand.mobile.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Account {

	@JsonIgnore
	private String sndaId;
	
	private String uid;
	private String usernameNorm;
	private String displayName;
	private String email;
	private String locale;
	private DateTime creationTime;
	private DateTime modifiedTime;
	private boolean available;
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getUsernameNorm() {
		return usernameNorm;
	}

	public void setUsernameNorm(String usernameNorm) {
		this.usernameNorm = usernameNorm;
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
	
	public DateTime getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}
	
	public DateTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(DateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getSndaId() {
		return sndaId;
	}

	public void setSndaId(String sndaId) {
		this.sndaId = sndaId;
	}

}
