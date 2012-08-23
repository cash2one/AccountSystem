package com.snda.gcloud.as.mongo.model;


public class Device {
	
	private String name;
	private long lastLogin;
	private String ipv4;
	
	public Device(String name, long lastLogin, String ipv4) {
		this.name = name;
		this.lastLogin = lastLogin;
		this.ipv4 = ipv4;
	}
	
	public String getName() {
		return name;
	}
	
	public long getLastLogin() {
		return lastLogin;
	}
	
	public String getIpv4() {
		return ipv4;
	}
	
	@Override
	public String toString() {
		return "Device [name=" + name +
				", last_login=" + lastLogin +
				", ipv4=" + ipv4 +
				"]";
	}
	
}
