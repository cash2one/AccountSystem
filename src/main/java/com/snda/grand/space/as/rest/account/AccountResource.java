package com.snda.grand.space.as.rest.account;

import javax.ws.rs.core.Response;

import com.snda.grand.space.as.rest.model.Account;



public interface AccountResource {

	Account create(String sndaId, String displayName, String email, String locale);
	
	Response modify(String sndaId, String displayName, String email, String locale);
	
	Response available(String sndaId, String available);
	
	Response status(String sndaId);
	
	Response applications(String sndaId);
	
}
