package com.codepath.apps.mytwitter.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.TwitterApplication;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.apps.mytwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Admin on 7/6/2017.
 */

public class RetweetDialog extends DialogFragment{



    @BindView(R.id.etRetweet)
    EditText etRetweet;


    @BindView(R.id.tvNickName)
    TextView tvNick;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvContent)
    TextView tvContent;

    @BindView(R.id.ivAvatar)
    ImageView mAvartar;

    private Tweet tweet;
    private TwitterClient twitterClient;
    private PostTweetDialogFragment.CallbackSuccess callback;

    public void setCallback(PostTweetDialogFragment.CallbackSuccess callback) {
        this.callback = callback;
    }

    public static RetweetDialog newInstance(Tweet tweet) {

        Bundle args = new Bundle();
        args.putParcelable("item",tweet);
        RetweetDialog fragment = new RetweetDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retweet,container,false);
        ButterKnife.bind(this, view);
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        twitterClient = TwitterApplication.getRestClient();
        tweet = getArguments().getParcelable("item");
        tvName.setText(tweet.getUser().getName());
        tvContent.setText(tweet.getContent());
        tvNick.setText("@"+tweet.getUser().getScreenName());

        Glide.with(getContext())
                .load(tweet.getUser().getUrlAvatar())
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(mAvartar);

    }

    @OnClick(R.id.btnRetweet)
    public void retweet() {
        onRetweet();
    }

    @OnClick(R.id.btnClose)
    public void closeDialog() {
        Log.d("KOKO","1111");
        getDialog().dismiss();
    }

    public void onRetweet() {

        twitterClient.onRetweet(tweet.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("retweeted_status");
                    Log.d("KKKKKK",object.toString());
                    callback.onCreateSuccess(Tweet.parseJson(object));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public interface CallBackSuccess{
        void onSuccess(Tweet tweet);
    }
}
