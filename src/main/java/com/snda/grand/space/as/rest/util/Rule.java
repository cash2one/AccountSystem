package com.snda.grand.space.as.rest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.snda.grand.space.as.exception.InvalidEmailException;


public final class Rule {
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("\\w(\\.\\w)*@\\w(\\.\\w)+");
	
	private Rule() {}
	
	public static void checkEmail(String email) {
		if (email != null) {
			Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
			if (!emailMatcher.matches()) {
				throw new InvalidEmailException();
			}
		} else {
			throw new InvalidEmailException();
		}
	}

}
