package com.codepath.apps.mytwitter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.TwitterApplication;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.apps.mytwitter.Utity.DateUtity;
import com.codepath.apps.mytwitter.fragment.PostTweetDialogFragment;
import com.codepath.apps.mytwitter.fragment.TweetListFragment;
import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin on 7/2/2017.
 */

public class TimeLineActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.main_logo);
        startFragment();
    }

    private void startFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutContain, TweetListFragment.newInstance())
                .commit();
    }

}
