package com.snda.grand.mobile.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown=true)
public class SdoValidationData {

	@JsonProperty("appId")
	private String appId;
	
	@JsonProperty("autoLoginFlag")
	private int autoLoginFlag;
	
	@JsonProperty("displayAccount")
	private String displayAccount;
	
	@JsonProperty("endpointIp")
	private String endpointIp;
	
	@JsonProperty("sndaId")
	private String sndaId;
	
	@JsonProperty("inputUserId")
	private String inputUserId;
	
	public String getAppId() {
		return appId;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getAutoLoginFlag() {
		return autoLoginFlag;
	}

	public void setAutoLoginFlag(int autoLoginFlag) {
		this.autoLoginFlag = autoLoginFlag;
	}

	public String getDisplayAccount() {
		return displayAccount;
	}

	public void setDisplayAccount(String displayAccount) {
		this.displayAccount = displayAccount;
	}

	public String getEndpointIp() {
		return endpointIp;
	}

	public void setEndpointIp(String endpointIp) {
		this.endpointIp = endpointIp;
	}

	public String getSndaId() {
		return sndaId;
	}

	public void setSndaId(String sndaId) {
		this.sndaId = sndaId;
	}

	public String getInputUserId() {
		return inputUserId;
	}

	public void setInputUserId(String inputUserId) {
		this.inputUserId = inputUserId;
	}
	
}
