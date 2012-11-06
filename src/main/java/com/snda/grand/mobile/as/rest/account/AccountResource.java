package com.snda.grand.mobile.as.rest.account;

import java.util.List;

import com.snda.grand.mobile.as.rest.model.Account;
import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.rest.model.Authorization;


public interface AccountResource {

	Account create(String sndaId, String usernameNorm, String displayName, String email, String locale);
	
	Account modify(String sndaId, String displayName, String email, String locale);
	
	Account available(String sndaId, String available);
	
	Account status(String sndaId);
	
	List<Application> applications(String sndaId);
	
	List<Authorization> listAuthorizations(String sndaId);
	
}
