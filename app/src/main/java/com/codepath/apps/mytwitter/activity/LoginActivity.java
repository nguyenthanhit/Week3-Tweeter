package com.codepath.apps.mytwitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@BindView(R.id.toolBar)
	Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		startActivity(new Intent(this,TimeLineActivity.class));
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}


	public void loginToRest(View view) {
		getClient().connect();
	}

}
