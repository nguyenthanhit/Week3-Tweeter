package com.codepath.apps.mytwitter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mytwitter.fragment.TweetListFragment;
import com.codepath.apps.mytwitter.fragment.UserProfileFragment;

/**
 * Created by Admin on 7/10/2017.
 */

public class AdapterFragment extends FragmentPagerAdapter {

    public AdapterFragment(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case  0:
                return TweetListFragment.newInstance();
            case 2:
                return UserProfileFragment.newInstance();
            default:
                return TweetListFragment.newInstance();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 2:
                return "MyProfile";
        }
        return null;
    }
}
