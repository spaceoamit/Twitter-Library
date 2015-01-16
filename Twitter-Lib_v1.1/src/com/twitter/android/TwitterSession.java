package com.twitter.android;

import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class TwitterSession {
	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String TWEET_AUTH_KEY = "authKey";
	private static final String TWEET_AUTH_SECRET_KEY = "authSecret";
	private static final String TWEET_USER_NAME = "Uname";
	private static final String SHARED = "Twitter_Preferences";

	public TwitterSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		
	}

	public void storeAccessToken(AccessToken accessToken, String username) {
		editor = sharedPref.edit();
		editor.putString(TWEET_AUTH_KEY, accessToken.getToken());
		editor.putString(TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(TWEET_USER_NAME, username);
		editor.commit();
		
		/*Log.e("log_tag","accessToken.getToken(): "+accessToken.getToken());
		Log.e("log_tag","accessToken.getTokenSecret():  "+accessToken.getTokenSecret());*/
	}

	public void resetAccessToken() {
		editor.putString(TWEET_AUTH_KEY, null);
		editor.putString(TWEET_AUTH_SECRET_KEY, null);
		editor.putString(TWEET_USER_NAME, null);

		editor.commit();
		
		//Log.e("log_tag","resetAccessToken :  called");
		
	}

	public String getUsername() {
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

	public AccessToken getAccessToken() {
		String token = sharedPref.getString(TWEET_AUTH_KEY, null);
		String tokenSecret = sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);
		
		
	/*	Log.e("log_tag","token1: "+token);
		Log.e("log_tag","tokenSecret:  "+tokenSecret);*/

		if (token != null && tokenSecret != null)
			return new AccessToken(token, tokenSecret);
		else
			return null;
	}

}
