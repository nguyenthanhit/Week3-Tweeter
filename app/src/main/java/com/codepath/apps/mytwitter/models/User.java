package com.codepath.apps.mytwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 7/3/2017.
 */

public class User implements Parcelable  {

    public User(){}

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("profile_image_url_https")
    private String urlAvatar;

    @SerializedName("followers_count")
    private String followersCount;

    @SerializedName("friends_count")
    private String friendsCount;

    @SerializedName("statuses_count")
    private String statusCount;

    public String getFollowersCount() {
        return followersCount;
    }

    public String getFriendsCount() {
        return friendsCount;
    }

    public String getstatusCountCount() {
        return statusCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR
            = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(screenName);
        dest.writeString(urlAvatar);

    }

    private User(Parcel in){
        id =in.readLong();
        name = in.readString();
        screenName = in.readString();
        urlAvatar = in.readString();
    }
}
