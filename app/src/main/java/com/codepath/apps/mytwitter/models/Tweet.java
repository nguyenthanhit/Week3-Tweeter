package com.codepath.apps.mytwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

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

public class Tweet  implements Parcelable{
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

    public Tweet(Parcel in) {
        id = in.readLong();
        content = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        createAt = in.readString();
    }


    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    private Media media;

    @SerializedName("user")
    private User user;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(content);
        dest.writeString(createAt);
        dest.writeValue(user);
    }

    public static  final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

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
    public static Tweet parseJson(JSONObject response){
                Gson gson = new Gson();
                Tweet tweet = null;
                try {
                tweet = gson.fromJson(String.valueOf(response),Tweet.class);
                if (response.getJSONObject("entities").has("media")) {
                    Tweet.Media media  = gson.fromJson(
                            String.valueOf(response.getJSONObject("entities")
                                    .getJSONArray("media").getJSONObject(0)), Tweet.Media.class);
                    tweet.setMedia(media);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
      return  tweet;
    }
}
