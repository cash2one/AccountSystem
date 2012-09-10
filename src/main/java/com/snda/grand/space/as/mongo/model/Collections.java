package com.snda.grand.space.as.mongo.model;

public class Collections {
	
	public static final String ACCOUNT_COLLECTION_NAME = "accounts";
	
	public static final String APPLICATION_COLLECTION_NAME = "applications";
	
	public static final String AUTHORIZATION_COLLECTION_NAME = "authorizations";
	
	public static final String TOKEN_COLLECTION_NAME = "tokens";
	
	public static final String CODE_COLLECTION_NAME = "codes";
	
	public static final long CODE_EXPIRE_TIME = 60 * 60 * 1000L;
	
	public static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;
	
	public static final String ACCESSOR_COLLECTION_NAME = "accessors";
	
	public abstract class Account {
		public static final String SNDA_ID = "snda_id";
		public static final String UID = "uid";
		public static final String USERNAME_NORM = "username_norm";
		public static final String DISPLAY_NAME = "display_name";
		public static final String EMAIL = "email";
		public static final String LOCALE = "locale";
		public static final String CREATION_TIME = "creation_time";
		public static final String MODIFIED_TIME = "modified_time";
		//TODO Devices definations
		public static final String AVAILABLE = "available";
	}

	public abstract class Application {
		public static final String APPID = "appid";
		public static final String APP_DESCRIPTION = "app_description";
		public static final String APP_STAUTS = "app_status";
		public static final String APP_KEY = "app_key";
		public static final String APP_SECRET = "app_secret";
		public static final String SCOPE = "scope";
		public static final String WEBSITE = "website";
		public static final String CREATION_TIME = "creation_time";
		public static final String MODIFIED_TIME = "modified_time";
		public static final String OWNER = "owner";
	}
	
	public abstract class Authorization {
		public static final String UID = "uid";
		public static final String APPID = "appid";
		public static final String REFRESH_TOKEN = "refresh_token";
		public static final String AUTHORIZED_TIME = "authorized_time";
	}
	
	public abstract class Token {
		public static final String REFRESH_TOKEN = "refresh_token";
		public static final String ACCESS_TOKEN = "access_token";
		public static final String CREATION_TIME = "creation_time";
		public static final String EXPIRE = "expire";
	}
	
	public abstract class Code {
		public static final String CODE = "code";
		public static final String REDIRECT_URI = "redirect_uri";
		public static final String UID = "uid";
		public static final String APPID = "appid";
		public static final String CREATION_TIME = "creation_time";
	}
	
	public abstract class Accessor {
		public static final String USERNAME = "username";
		public static final String ACCESS_KEY = "access_key";
		public static final String SECRET_KEY = "secret_key";
		public static final String DESCRIPTION = "description";
		public static final String CREATION_TIME = "creation_time";
	}
	
}
