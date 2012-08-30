package com.snda.grand.space.as.rest.util;

import java.math.BigInteger;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import com.google.common.base.Charsets;

public final class ApplicationKeys {
	
	private ApplicationKeys() {
	}

	public static String generateAccessKeyId() {
		return new BigInteger(uuid().replaceAll("-", ""), 16).toString(36).toUpperCase();
	}

	public static String generateSecretAccessKey() {
		return new String(Base64.encodeBase64(uuid().getBytes(Charsets.UTF_8), false), Charsets.UTF_8);
	}
	
	private static String uuid() {
		return UUID.randomUUID().toString();
	}
	
}