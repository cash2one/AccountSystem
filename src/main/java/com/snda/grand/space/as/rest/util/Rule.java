package com.snda.grand.space.as.rest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Rule {
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private static final Pattern DOMAIN_PATTER = Pattern.compile("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?");
	
	private Rule() {}
	
	public static boolean checkEmail(String email) {
		if (email != null) {
			Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
			if (emailMatcher.matches()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkDomain(String domain) {
		if (domain != null) {
			Matcher domainMatcher = DOMAIN_PATTER.matcher(domain);
			if (domainMatcher.matches()) {
				return true;
			}
		}
		return false;
	}

}
