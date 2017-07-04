package com.codepath.apps.mytwitter;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.twitter.sdk.android.core.Twitter;

import android.app.Application;
import android.content.Context;

import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = TwitterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		TwitterApplication.context = this;
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
	}

	public static twitter4j.Twitter getInstance(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(TwitterClient.REST_CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterClient.REST_CONSUMER_SECRET)
				.setOAuthAccessToken("3295492988-98WocTfiw2bwP63vQtTDo9q6Sa2cfyzKCrG9VvQ")
				.setOAuthAccessTokenSecret("RXgbTP8adbr9IUi89QMX84iYJnuxgNsvbLp0oAUea5gZd");
		TwitterFactory twitterFactory = new TwitterFactory(cb.build());
		return twitterFactory.getInstance();
	}
}