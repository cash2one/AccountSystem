package com.snda.grand.space.as.rest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Rule {
	
	public static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	public static final Pattern DOMAIN_PATTERN = Pattern.compile("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$");
	public static final Pattern SUB_DOMAIN_PATTERN = Pattern.compile("([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}[/:]");
	
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
			Matcher domainMatcher = DOMAIN_PATTERN.matcher(domain);
			if (domainMatcher.matches()) {
				return true;
			}
		}
		return false;
	}

}
