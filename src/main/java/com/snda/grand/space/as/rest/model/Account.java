package com.snda.grand.space.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Account {

	private String uid;
	private String display_name;
	private String email;
	private String locale;
	private DateTime creationTime;
	private boolean available;
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getDisplay_name() {
		return display_name;
	}
	
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
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
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
}
