package com.codepath.apps.mytwitter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.TwitterApplication;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.apps.mytwitter.adapter.TweetAdapter;
import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * Created by Admin on 7/3/2017.
 */

public class TweetListFragment extends Fragment implements TweetAdapter.Listener{

    @BindView(R.id.rvTweets)
    RecyclerView mTweetList;

    @BindView(R.id.pbLoad)
    ProgressBar mLoad;

    @BindView(R.id.pbLoadMore)
    ProgressBar mLoadMore;

    @BindView(R.id.fbTweet)
    FloatingActionButton btnTweet;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TweetAdapter mTweetAdapter;
    private TwitterClient client;
    PostTweetDialogFragment fragmenttPost;
    RetweetDialog retweetDialog;
    private  User mUser;
    private List<Tweet> tweets = new ArrayList<>();
    public static TweetListFragment newInstance() {
        Bundle args = new Bundle();
        TweetListFragment fragment = new TweetListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_tweet,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = TwitterApplication.getRestClient();
        mTweetAdapter = new TweetAdapter(getContext());
        mTweetAdapter.setmListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mTweetList.setLayoutManager(layoutManager);
        ((DefaultItemAnimator) mTweetList.getItemAnimator()).setSupportsChangeAnimations(false);
        mTweetList.setAdapter(mTweetAdapter);
        loadData();
        getCurrentUser(new ListenerUser() {
            @Override
            public void onResult(User user) {
                mUser = user;
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(() -> refresh());

        btnTweet.setOnClickListener(
                v -> {
                   newPost();
                }
        );

    }

    private void newPost() {
        fragmenttPost = PostTweetDialogFragment.newInstance(mUser);
        fragmenttPost.setmCallbackSuccess(new PostTweetDialogFragment.CallbackSuccess() {
            @Override
            public void onCreateSuccess(Tweet tweet) {
                mTweetAdapter.addOneTweet(tweet);
            }
        });
        fragmenttPost.show(getFragmentManager(),"");
    }

    private void refresh(){
        TwitterClient.page = 1;
        fetchTweets(tweets -> {
            mTweetAdapter.setData(tweets);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void loadData() {
        mLoad.setVisibility(View.VISIBLE);
        fetchTweets(tweets -> {
            mTweetAdapter.setData(tweets);
            mLoad.setVisibility(View.GONE);
        });

    }
    private void loadMore() {
        TwitterClient.page += 1;
        mLoadMore.setVisibility(View.VISIBLE);

        fetchTweets(tweets -> {
            mTweetAdapter.addData(tweets);
            mLoadMore.setVisibility(View.GONE);
        });
    }
    private void fetchTweets(final Listener listener) {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweets = Tweet.parseJson(response);
                listener.onResult(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void getCurrentUser(ListenerUser listenerUser) {
        final User[] user = {new User()};
        client.getMyAccount(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                user[0] = gson.fromJson(String.valueOf(response),User.class);
                listenerUser.onResult(user[0]);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("TKS",responseString);
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
    public void onLike(long id) {

        client.favouriTweet(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                mTweetAdapter.updateData(Tweet.parseJson(response));
            }
        });
    }

    @Override
    public void unLike(long id) {
        client.unFavorite(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                mTweetAdapter.updateData(Tweet.parseJson(response));
            }
        });
    }

    @Override
    public void onShowDialogShare(Tweet tweet) {
        retweetDialog = RetweetDialog.newInstance(tweet);
        retweetDialog.setCallback(new PostTweetDialogFragment.CallbackSuccess() {
            @Override
            public void onCreateSuccess(Tweet tweet) {
                mTweetAdapter.updateData(tweet);
            }
        });
        retweetDialog.show(getFragmentManager(),"");
    }

    @Override
    public void onReply(long idTweet) {

    }


    public interface Listener {
        void onResult(List<Tweet> tweets);
    }

    public interface ListenerUser{
        void onResult(User user);
    }
}
