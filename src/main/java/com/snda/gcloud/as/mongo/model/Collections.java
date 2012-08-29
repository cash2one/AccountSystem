package com.snda.gcloud.as.mongo.model;

public class Collections {
	
	public static final String ACCOUNT_COLLECTION_NAME = "accounts";
	
	public static final String APPLICATION_COLLECTION_NAME = "applications";
	
	public static final String TOKEN_COLLECTION_NAME = "tokens";
	
	public abstract class Account {
		public static final String SNDA_ID = "snda_id";
		public static final String UID = "uid";
		public static final String DISPLAY_NAME = "display_name";
		public static final String EMAIL = "email";
		public static final String LOCALE = "locale";
		public static final String CREATION_TIME = "creation_time";
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
		public static final String OWNER = "owner";
	}
	
	public abstract class Token {
		public static final String UID = "uid";
		public static final String APPID = "appid";
		public static final String TOKEN = "token";
		public static final String EXPIRE = "expire";
	}
	
}
