package com.codepath.apps.mytwitter;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();// Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "3VeljZDOJY5rmT0IKgkjRTjhE";       // Change this
	public static final String REST_CONSUMER_SECRET = "dKhBy9ccLFmTnHCJCWToq9CRa12eK13omg8R4ehFNoEaXLIDlo"; // Change this

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "oauth://demoTweeter";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";
	public static final String USER_ID = "3295492988";
	public static final String SCREEN_NAME="yannguyen9it";

	public static int page = 1;

	public void setPage(int page) {
		this.page = page;
	}

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}


	public void getHomeTimeLine(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 20);
		params.put("page",String.valueOf(page));
        params.put("since_id",1);
		getClient().get(apiUrl,params,handler);

	}

	public void postTweet (String text , AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",text.trim());
		getClient().post(apiUrl,params,handler);

	}
	public void getMyAccount(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl,handler);
	}

	public void favouriTweet (long id, AsyncHttpResponseHandler handler) {

		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id",id);
		getClient().post(apiUrl,params,handler);

	}

	public void unFavorite (long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id",id);
		getClient().post(apiUrl,params,handler);

	}

	public void onRetweet(long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/retweet/"+String.valueOf(id)+".json");
		getClient().post(apiUrl,handler);
	}

	public void getUserTimeLine(AsyncHttpResponseHandler handler) {

		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("user_id",USER_ID);
		params.put("screen_name",SCREEN_NAME);
		params.put("page",String.valueOf(page));
		getClient().get(apiUrl,params,handler);
	}
}
