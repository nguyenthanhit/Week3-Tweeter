package com.codepath.apps.mytwitter.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 7/2/2017.
 */

public class Tweet {
    @SerializedName("text")
    private String content;

    @SerializedName("created_at")
    private String createAt;

    @SerializedName("id")
    private long id;

    @SerializedName("favorited")
    private boolean isFavorite;

    @SerializedName("retweeted")
    private boolean isRetweet;

    @SerializedName("retweet_count")
    private int retweetcount;

    @SerializedName("favorite_count")
    private int favouritecount;


    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    private Media media;

    @SerializedName("user")
    private User user;

    public class Media {
        @SerializedName("media_url_https")
        private String url;

        public String getUrl() {
            return url;
        }
    }

    public String getContent() {
        return content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public long getId() {
        return id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public int getRetweetcount() {
        return retweetcount;
    }

    public int getFavouritecount() {
        return favouritecount;
    }

    public User getUser() {
        return user;
    }

    public static List<Tweet> parseJson(JSONArray response){
        List<Tweet> tweets = new ArrayList<>();
        for (int i=0 ; i < response.length(); i++) {
            try {
                Gson gson = new Gson();
                JSONObject object = response.getJSONObject(i);
                Tweet tweet = gson.fromJson(String.valueOf(object),Tweet.class);
                if (object.getJSONObject("entities").has("media")) {
                    Tweet.Media media  = gson.fromJson(
                            String.valueOf(object.getJSONObject("entities")
                                    .getJSONArray("media").getJSONObject(0)), Tweet.Media.class);
                    tweet.setMedia(media);
                }
                tweets.add(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }
}
