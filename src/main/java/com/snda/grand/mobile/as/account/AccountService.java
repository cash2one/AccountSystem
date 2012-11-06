package com.snda.grand.mobile.as.account;

import com.snda.grand.mobile.as.rest.model.Account;

public interface AccountService {

	Account putAccount(String sndaId, String uid, String usernameNorm,
			String displayName, String email, String locale, boolean available,
			long creationTime, long modifiedTime);
	
	Account updateAccount(String sndaId, String uid, String usernameNorm,
			String displayName, String email, String locale, boolean available,
			long creationTime, long modifiedTime);
	
	Account getAccountBySndaId(String sndaId);
	
	Account getAccountByUid(String uid);
	
	void deleteAccountBySndaId(String sndaId);
	
}
