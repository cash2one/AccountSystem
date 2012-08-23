package com.snda.gcloud.as.account.rest;

import javax.ws.rs.core.Response;



public interface AccountResource {

	Response create(String sndaId, String username, String email, String locale);
	
	Response modify();
	
	Response disable();
	
}
