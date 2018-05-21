package com.simhuang.trivial.firebaseUtils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.R;
import com.simhuang.trivial.fragments.UserProfileFragment;
import com.simhuang.trivial.model.User;

/**
 * This class contains static methods for accessing common data
 * in the users table in firebase db.
 */
public class UserUtils {

    private static FirebaseAuth mAuth;
    private static DatabaseReference mUserReference;

    public static void retrieveUserProfileData(final FragmentManager fragmentManager) {
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        mUserReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Bundle args = new Bundle();
                args.putInt("token", user.getToken());
                args.putInt("gamesWon", user.getGamesWon());
                args.putInt("gamesLost", user.getGamesLost());
                args.putString("username", user.getUsername());

//                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                UserProfileFragment userProfileFragment = new UserProfileFragment();
                userProfileFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container, userProfileFragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //IGNORING FAILED DATABASE CALLS FOR NOW
            }
        });
    }
}
