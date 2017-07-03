package com.codepath.apps.mytwitter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin on 7/3/2017.
 */

public class TweetListFragment extends Fragment implements TweetAdapter.Listener {

    @BindView(R.id.rvTweets)
    RecyclerView mTweetList;

    @BindView(R.id.pbLoad)
    ProgressBar mLoad;

    @BindView(R.id.pbLoadMore)
    ProgressBar mLoadMore;



    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TweetAdapter mTweetAdapter;
    private TwitterClient client;


    public static TweetListFragment newInstance() {
        Bundle args = new Bundle();
        TweetListFragment fragment = new TweetListFragment();
        fragment.setArguments(args);
        return fragment;
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
        mTweetList.setAdapter(mTweetAdapter);
        loadData();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

    }

    private void refresh(){
        TwitterClient.refreshPage();
        fetchTweets(new Listener() {
            @Override
            public void onResult(List<Tweet> tweets) {
                mTweetAdapter.setData(tweets);

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadData() {
        mLoad.setVisibility(View.VISIBLE);
        fetchTweets(new Listener() {
            @Override
            public void onResult(List<Tweet> tweets) {
                Log.d("KKKK",tweets.size()+"");
                mTweetAdapter.setData(tweets);
                mLoad.setVisibility(View.GONE);
            }
        });

    }
    private void loadMore() {
        mLoadMore.setVisibility(View.VISIBLE);
        TwitterClient.setPage();
        fetchTweets(new Listener() {
            @Override
            public void onResult(List<Tweet> tweets) {
                mTweetAdapter.addData(tweets);
                mLoadMore.setVisibility(View.GONE);
            }
        });
    }
    private void fetchTweets(final Listener listener) {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<Tweet> mTweets = Tweet.parseJson(response);
                listener.onResult(mTweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
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

    public interface Listener {
        void onResult(List<Tweet> tweets);
    }


}
