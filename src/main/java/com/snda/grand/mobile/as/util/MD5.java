package com.snda.grand.mobile.as.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class MD5 {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private MD5() {
	}

	public static String digest(byte[] bytes) {
		MessageDigest md5 = getInstance();
		md5.update(bytes);
		return toString(md5);
	}
	
	public static String hexDigest(byte[] bytes) {
		MessageDigest md5 = getInstance();
		md5.update(bytes);
		return Hex.encodeHexString(md5.digest());
	}

	public static String toString(MessageDigest md5) {
		return base64String(md5.digest());
	}
	
	public static String base64String(byte[] input) {
		byte[] base64Bytes = Base64.encodeBase64(input, false);
		return new String(base64Bytes, UTF_8);
	}
	
	public static MessageDigest getInstance() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}
}