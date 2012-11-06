package com.snda.grand.mobile.as.rest.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.Maps;

public final class Locales {
	
	private static Map<String, Locale> locales = Maps.newConcurrentMap();
	
	static {
		Locale localeList[] = SimpleDateFormat.getAvailableLocales();
		for (Locale locale : localeList) {
			locales.put(locale.toString(), locale);
		}
	}
	
	public static boolean containsLocale(String locale) {
		return locales.containsKey(locale);
	}

}
