package com.codepath.apps.mytwitter.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.TwitterApplication;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by Admin on 7/3/2017.
 */

public class PostTweetDialogFragment extends DialogFragment {
    @BindView(R.id.ibClose)
    ImageButton btnClose;

    @BindView(R.id.etContent)
    EditText mContent;

    @BindView(R.id.btnTweet)
    Button btnTweet;

    @BindView(R.id.tvNickName)
    TextView tvNick;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.ivAvatar2)
    ImageView mAvartar;

    @BindView(R.id.tvNumberText)
    TextView tvNumberText;
    private CallbackSuccess mCallbackSuccess;
    private  User user;
    private int numberofText = 200;
    private TwitterClient twitterClient;

    public void setmCallbackSuccess(CallbackSuccess mCallbackSuccess) {
        this.mCallbackSuccess = mCallbackSuccess;
    }


    public static PostTweetDialogFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable("user",user);
        PostTweetDialogFragment  fragment= new PostTweetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static PostTweetDialogFragment newInstance() {
        Bundle args = new Bundle();
        PostTweetDialogFragment  fragment= new PostTweetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_tweet,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
        mContent.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        twitterClient = TwitterApplication.getRestClient();
        tvNumberText.setText(numberofText + "");
        btnClose.setOnClickListener(v -> {
            dismiss();
        });
        btnTweet.setOnClickListener(v -> {
            UpdateStatus();
            mContent.setText("");

        });
        mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(numberofText)});

        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numberofText -= 1;
                tvNumberText.setText(numberofText + "");

                if (count == 0) {
                    numberofText +=1;
                    tvNumberText.setText(numberofText+"");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("kk",s.toString());
            }
        });
    }

    private void setData() {
        user = getArguments().getParcelable("user");
        tvName.setText(user.getName());
        tvNick.setText("@" +user.getScreenName());
        Glide.with(getContext())
                .load(user.getUrlAvatar())
                .bitmapTransform(new RoundedCornersTransformation(getContext(),3,2))
                .into(mAvartar);
    }

    public interface  CallbackSuccess {
        void onCreateSuccess(Tweet tweet);
    }

    public void UpdateStatus() {
        String content = mContent.getText().toString();
        twitterClient.postTweet(content, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mCallbackSuccess.onCreateSuccess(Tweet.parseJson(response));

            }
        });
    }

}
