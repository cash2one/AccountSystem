package com.snda.grand.space.as.account.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import com.snda.grand.space.as.account.ApplicationService;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.rest.model.Application;

public class MongoApplicationService implements ApplicationService {
	
	private final MongoOperations mongoOps;
	
	public MongoApplicationService(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	@Override
	public Application putApplication(String appId, String uid, String appKey, 
			String appSecret, String appDescription,
			String appStatus, String publisherName, String scope,
			String website, long creationTime, long modifiedTime) {
		PojoApplication application = new PojoApplication(appId,
				appDescription, appStatus, appKey, appSecret, publisherName,
				scope, website, creationTime, modifiedTime, uid);
		mongoOps.insert(application, MongoCollections.APPLICATION_COLLECTION_NAME);
		return newApplication(application);
	}
	
	@Override
	public Application updateApplication(String appId, String modifiedAppId, 
			String uid, String appKey, String appSecret, String appDescription,
			String appStatus, String publisherName, String scope,
			String website, long creationTime, long modifiedTime) {
		Update update = new Update()
							.set(MongoCollections.Application.APPID, modifiedAppId)
							.set(MongoCollections.Application.OWNER, uid)
							.set(MongoCollections.Application.APP_KEY, appKey)
							.set(MongoCollections.Application.APP_SECRET, appSecret)
							.set(MongoCollections.Application.APP_DESCRIPTION, appDescription)
							.set(MongoCollections.Application.APP_STAUTS, appStatus)
							.set(MongoCollections.Application.PUBLISHER_NAME, publisherName)
							.set(MongoCollections.Application.SCOPE, scope)
							.set(MongoCollections.Application.WEBSITE, website)
							.set(MongoCollections.Application.CREATION_TIME, creationTime)
							.set(MongoCollections.Application.MODIFIED_TIME, modifiedTime);
		mongoOps.updateFirst(query(where(MongoCollections.Application.APPID)
				.is(appId)), update,
				MongoCollections.APPLICATION_COLLECTION_NAME);
		String newAppId = appId.equalsIgnoreCase(modifiedAppId) ? appId : modifiedAppId;
		return newApplication(newAppId, uid, appKey, appSecret, appDescription,
				appStatus, publisherName, scope, website, creationTime,
				modifiedTime);
	}

	@Override
	public Application getApplicationByAppId(String appId) {
		PojoApplication application = mongoOps.findOne(
				query(where(MongoCollections.Application.APPID).is(appId)),
				PojoApplication.class, MongoCollections.APPLICATION_COLLECTION_NAME);
		return newApplication(application);
	}

	@Override
	public void deleteApplication(String appId) {
		mongoOps.remove(
				query(where(MongoCollections.Application.APPID).is(appId)),
				MongoCollections.APPLICATION_COLLECTION_NAME);
	}
	
	@Override
	public List<Application> listApplications(String owner) {
		List<PojoApplication> apps = mongoOps.find(
				query(where(MongoCollections.Application.OWNER).is(owner)),
				PojoApplication.class, MongoCollections.APPLICATION_COLLECTION_NAME);
		return PojoApplication.getApplications(apps);
	}
	
	private Application newApplication(PojoApplication pojoApplication) {
		Application application = null;
		if (pojoApplication != null) {
			application = pojoApplication.getApplication();
		}
		return application;
	}
	
	private Application newApplication(String appId, String uid,
			String appKey, String appSecret, String appDescription,
			String appStatus, String publisherName, String scope,
			String website, long creationTime, long modifiedTime) {
		Application application = new Application();
		application.setAppid(appId);
		application.setOwner(uid);
		application.setAppKey(appKey);
		application.setAppSecret(appSecret);
		application.setAppDescription(appDescription);
		application.setAppStatus(appStatus);
		application.setPublisherName(publisherName);
		application.setScope(scope);
		application.setWebsite(website);
		application.setCreationTime(new DateTime(creationTime));
		application.setModifiedTime(new DateTime(modifiedTime));
		return application;
	}

}
