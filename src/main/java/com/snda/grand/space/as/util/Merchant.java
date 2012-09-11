package com.snda.grand.space.as.util;


public class Merchant {
	
	private String areaId;
	
	private String appId;
	
	private String customSecurityLevel;
	
	private String merchantName;
	
	private String signatureMethod;
	
	private String md5SecretKey;
	
//	public Merchant(String areaId, String appId, String customSecurityLevel,
//			String merchantName, String signatureMethod, String md5SecretKey) {
//		Merchant.areaId = areaId;
//		Merchant.appId = appId;
//		Merchant.customSecurityLevel = customSecurityLevel;
//		Merchant.merchantName = merchantName;
//		Merchant.signatureMethod = signatureMethod;
//		Merchant.md5SecretKey = md5SecretKey;
//	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCustomSecurityLevel() {
		return customSecurityLevel;
	}

	public void setCustomSecurityLevel(String customSecurityLevel) {
		this.customSecurityLevel = customSecurityLevel;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getSignatureMethod() {
		return signatureMethod;
	}

	public void setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
	}

	public String getMd5SecretKey() {
		return md5SecretKey;
	}

	public void setMd5SecretKey(String md5SecretKey) {
		this.md5SecretKey = md5SecretKey;
	}

}
