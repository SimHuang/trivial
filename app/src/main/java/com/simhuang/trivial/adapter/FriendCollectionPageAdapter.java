package com.simhuang.trivial.adapter;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simhuang.trivial.fragments.UserFriendsFragment;
import com.simhuang.trivial.fragments.UserSearchFragment;

public class FriendCollectionPageAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private String[] tabTitles = new String[]{"Friends", "Search"};

    public FriendCollectionPageAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        switch(position) {
            case 0:
                //USER FRIEND FRAGMENT
                UserFriendsFragment userFriendsFragment = new UserFriendsFragment();
                return userFriendsFragment;
            case 1:
                //search friend fragment
                UserSearchFragment userSearchFragment = new UserSearchFragment();
                return userSearchFragment;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  tabTitles[position];
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
