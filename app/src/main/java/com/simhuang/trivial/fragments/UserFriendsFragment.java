package com.simhuang.trivial.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.R;
import com.simhuang.trivial.adapter.UserFriendsAdapter;
import com.simhuang.trivial.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class UserFriendsFragment extends Fragment {

    private RecyclerView friendsListRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private List<Friend> friendsList;
    private Context mContext;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_friends, container, false);

        mContext = view.getContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User_Friends").child(mAuth.getUid());

        fab = view.findViewById(R.id.fab);
        friendsListRecyclerView = view.findViewById(R.id.user_friends_recycler_view);
        layoutManager = new LinearLayoutManager(view.getContext());
        friendsListRecyclerView.setLayoutManager(layoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFABclick();
            }
        });

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsList = new ArrayList<>();
                //return the current user list under user
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Friend friend = snapshot.getValue(Friend.class);
                    friendsList.add(friend);
                }

                //render the recycler view after getting the friends list
                if(friendsList != null) {
                    mAdapter = new UserFriendsAdapter(friendsList, mContext);
                    friendsListRecyclerView.setLayoutManager(layoutManager);
                    friendsListRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //IGNORE IMPLEMENTATION
            }
        });

        return view;
    }

    public void handleFABclick() {
        UserSearchFragment userSearchFragment = new UserSearchFragment();
        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, userSearchFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}
