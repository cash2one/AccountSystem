package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class SdoValidateSignatureFailedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8578746883039449394L;

	@Override
	public String getMessage() {
		return "Sdo passport validate failed.";
	}

	@Override
	public Status getStatus() {
		return Status.FORBIDDEN;
	}

	@Override
	public String getCode() {
		return "SdoValidateSignatureFailed";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
