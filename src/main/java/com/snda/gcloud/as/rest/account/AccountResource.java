package com.snda.gcloud.as.rest.account;

import javax.ws.rs.core.Response;



public interface AccountResource {

	Response create(String sndaId, String displayName, String email, String locale);
	
	Response modify(String sndaId, String displayName, String email, String locale);
	
	Response available(String sndaId, String available);
	
	Response status(String sndaId);
	
	Response applications(String sndaId);
	
}
