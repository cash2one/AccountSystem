package com.snda.grand.space.as.rest.account;

import java.util.List;

import com.snda.grand.space.as.rest.model.Account;
import com.snda.grand.space.as.rest.model.Application;



public interface AccountResource {

	Account create(String sndaId, String displayName, String email, String locale);
	
	Account modify(String sndaId, String displayName, String email, String locale);
	
	Account available(String sndaId, String available);
	
	Account status(String sndaId);
	
	List<Application> applications(String sndaId);
	
}
