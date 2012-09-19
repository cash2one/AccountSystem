package com.snda.grand.space.as.processor;

import java.util.List;

import com.snda.grand.space.as.rest.model.Account;
import com.snda.grand.space.as.rest.model.Application;
import com.snda.grand.space.as.rest.model.Authorization;

public interface AccountResourceProcessor extends ResourceProcessor {

	Account create(String sndaId, String usernameNorm, String displayName, String email,
			String locale, boolean available);

	Account modify(String sndaId, String displayName, String email,
			String locale);

	Account available(String sndaId, boolean available);

	Account status(String sndaId);

	List<Application> applications(String sndaId);

	List<Authorization> listAuthorizations(String sndaId);
	
	void delete(String sndaId);

}
