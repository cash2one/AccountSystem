package com.snda.grand.mobile.as.rest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Rule {
	
	// Following regular expressions are come from http://regexlib.com.
	public static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	public static final Pattern DOMAIN_PATTERN = Pattern.compile("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$");
	public static final Pattern SUB_DOMAIN_PATTERN = Pattern.compile("^((([hH][tT][tT][pP][sS]?|[fF][tT][pP])\\:\\/\\/)?([\\w\\.\\-]+(\\:[\\w\\.\\&%\\$\\-]+)*@)?((([^\\s\\(\\)\\<\\>\\\\\\\"\\.\\[\\]\\,@;:]+)(\\.[^\\s\\(\\)\\<\\>\\\\\\\"\\.\\[\\]\\,@;:]+)*(\\.[a-zA-Z]{2,4}))|((([01]?\\d{1,2}|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d{1,2}|2[0-4]\\d|25[0-5])))(\\b\\:(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3}|0)\\b)?((\\/[^\\/][\\w\\.\\,\\?\\'\\\\\\/\\+&%\\$#\\=~_\\-@]*)*[^\\.\\,\\?\\\"\\'\\(\\)\\[\\]!;<>{}\\s\\x7F-\\xFF])?)$");
	public static final Pattern IP_V4_PATTERN = Pattern.compile("^((\\d|[1-9]\\d|2[0-4]\\d|25[0-5]|1\\d\\d)(?:\\.(\\d|[1-9]\\d|2[0-4]\\d|25[0-5]|1\\d\\d)){3})$");
	
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
	
	public static boolean checkIpv4(String ip) {
		if (ip != null) {
			Matcher domainMatcher = IP_V4_PATTERN.matcher(ip);
			if (domainMatcher.matches()) {
				return true;
			}
		}
		return false;
	}

}
