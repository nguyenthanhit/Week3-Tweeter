package com.codepath.apps.mytwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 7/3/2017.
 */

public class User   {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("profile_image_url_https")
    private String urlAvatar;

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


}
