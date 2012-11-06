package com.snda.grand.mobile.as.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown=true)
public class SdoValidation {
	
	@JsonProperty("data")
	private SdoValidationData data;
	
	@JsonProperty("return_code")
	private String returnCode;
	
	@JsonProperty("return_message")
	private String returnMessage;
	
	public SdoValidationData getData() {
		return data;
	}

	public void setData(SdoValidationData data) {
		this.data = data;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

}
