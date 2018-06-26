package com.simhuang.trivial.fragments;

import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simhuang.trivial.R;
import com.simhuang.trivial.adapter.FriendCollectionPageAdapter;
import com.simhuang.trivial.adapter.UserFriendsAdapter;

/**
 * This parent fragment will contain a view pager
 * and display both the swipable friends and search friends fragment
 */
public class UserFriendParentFragment extends Fragment {

    private final String FRIENDS_TAB = "Friends";
    private final String SEARCH_TAB = "Search";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FriendCollectionPageAdapter friendCollectionPageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_friend_parent, container, false);

        tabLayout = view.findViewById(R.id.friend_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(FRIENDS_TAB));
        tabLayout.addTab(tabLayout.newTab().setText(SEARCH_TAB));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        friendCollectionPageAdapter = new FriendCollectionPageAdapter(fragmentManager, tabLayout.getTabCount());

        viewPager = view.findViewById(R.id.friend_viewpager);
        viewPager.setAdapter(friendCollectionPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}

