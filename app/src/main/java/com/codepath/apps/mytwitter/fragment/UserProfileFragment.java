package com.codepath.apps.mytwitter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.TwitterApplication;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.apps.mytwitter.adapter.TweetAdapter;
import com.codepath.apps.mytwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin on 7/10/2017.
 */

public class UserProfileFragment extends Fragment implements TweetAdapter.Listener{

    @BindView(R.id.ivAvatar)
    ImageView cover;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvNickName)
    TextView tvNick;
    @BindView(R.id.tvNumeFollow)
    TextView tvFollow;
    @BindView(R.id.tvNumeFollower)
    TextView tvFollower;
    @BindView(R.id.tvNumTweets)
    TextView tvTweets;
    @BindView(R.id.rvTweetsWall)
    RecyclerView mListTweet;

    @BindView(R.id.pbLoadMore)
    ProgressBar mLoadMore;

    TweetAdapter tweetAdapter;
    TwitterClient twitterClient;
    Tweet tweet;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        twitterClient = TwitterApplication.getRestClient();
        tweetAdapter = new TweetAdapter(getContext());
        tweetAdapter.setmListener(this);
        mListTweet.setAdapter(tweetAdapter);
        mListTweet.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,false));
        mListTweet.setAdapter(tweetAdapter);

        getData(tweets -> {

            tweetAdapter.setData(tweets);
            tweet = tweets.get(0);
            setData();
        });
    }
     public void setData() {
        tvFollow.setText(tweet.getUser().getFriendsCount());
        tvFollower.setText(tweet.getUser().getFollowersCount());
        tvTweets.setText(tweet.getUser().getstatusCountCount());
        tvName.setText(tweet.getUser().getName());
        tvNick.setText("@"+tweet.getUser().getScreenName());
        Glide.with(getContext())
                .load(tweet.getUser().getUrlAvatar())
                .into(cover);
    }
    public static UserProfileFragment newInstance() {
        Bundle args = new Bundle();
        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void loadMore() {
        TwitterClient.page += 1;
        mLoadMore.setVisibility(View.VISIBLE);
        getData(tweets -> {
            tweetAdapter.addData(tweets);
            mLoadMore.setVisibility(View.GONE);
        });
    }

    private void getData(TweetListFragment.Listener listener) {
        twitterClient.getUserTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("VVVVVV",response.toString());
                listener.onResult(Tweet.parseJson(response));
            }

        });
    }

    @Override
    public void onClickItem(Tweet tweet) {

    }

    @Override
    public void onLoadMore() {
        loadMore();
    }

    @Override
    public void onLike(long idTweet) {

    }

    @Override
    public void unLike(long idTweet) {

    }

    @Override
    public void onShowDialogShare(Tweet tweet) {

    }

    @Override
    public void onReply(long idTweet) {

    }
}
