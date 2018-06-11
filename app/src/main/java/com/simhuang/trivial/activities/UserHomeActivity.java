package com.simhuang.trivial.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.R;
import com.simhuang.trivial.firebaseUtils.UserUtils;
import com.simhuang.trivial.fragments.LeaderboardFragment;
import com.simhuang.trivial.fragments.UserFriendsFragment;
import com.simhuang.trivial.fragments.UserProfileFragment;
import com.simhuang.trivial.fragments.UserSettingFragment;
import com.simhuang.trivial.model.User;

/**
 * This is the main activity once a user successfully log on to the app.
 * The user can see their profile, various stats, and play a trivia
 * game from this screen. This class is also responsible for retrieving
 * various web data for child fragments.
 */
public class UserHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private Toolbar mActionBar;
    private NavigationView navigationView;
//    private ProgressBar progressBar;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        mAuth = FirebaseAuth.getInstance();
        mDrawerLayout = findViewById(R.id.userhome_drawer_layout);
        mActionBar = findViewById(R.id.home_actionbar);

        setSupportActionBar(mActionBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Home");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);


//        progressBar.setIndeterminate(true);
//        progressBar.setVisibility(View.VISIBLE);

        setInitialLoadedFragment();

        navigationView = findViewById(R.id.nav_drawer);
        navigationView.setCheckedItem(R.id.profile);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                determineNavigationOnClick(item);

                return true;
            }
        });
    }

    /**
     * Determine the action to take if one of the nagivation drawer item has been clicked.
     */
    public void determineNavigationOnClick(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {

            case R.id.setting:
                UserSettingFragment userSettingFragment = new UserSettingFragment();
                fragmentTransaction.replace(R.id.fragment_container, userSettingFragment);
                fragmentTransaction.commit();
                break;

            case R.id.profile:
                UserUtils.retrieveUserProfileData(getSupportFragmentManager());
                break;

            case R.id.leaderboard:
                LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
                fragmentTransaction.replace(R.id.fragment_container, leaderboardFragment);
                fragmentTransaction.commit();
                break;

            case R.id.friends:
                UserFriendsFragment userFriendsFragment = new UserFriendsFragment();
                fragmentTransaction.replace(R.id.fragment_container, userFriendsFragment);
                fragmentTransaction.commit();
                break;

            case R.id.game:
                startNewGame();
                break;

            case R.id.exit:
                userLogout();
                break;

            default:
                break;
        }
    }

//    /**
//     * Retrieves user profile data from the 'User' json node base on the uid.
//     * The user object is passed into the profile fragment, and the profile fragment is rendered
//     * on this activity.
//     */
//    public void retrieveUserProfileData() {
//        String uid = mAuth.getCurrentUser().getUid();
//        mUserReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
//
//        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//
//                Bundle args = new Bundle();
//                args.putInt("token", user.getToken());
//                args.putInt("gamesWon", user.getGamesWon());
//                args.putInt("gamesLost", user.getGamesLost());
//                args.putString("username", user.getUsername());
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                UserProfileFragment userProfileFragment = new UserProfileFragment();
//                userProfileFragment.setArguments(args);
//                fragmentTransaction.replace(R.id.fragment_container, userProfileFragment);
//                fragmentTransaction.commit();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //IGNORING FAILED DATABASE CALLS FOR NOW
//            }
//        });
//    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.profile);
    }

    /**
     * Log user out of the app and return to the main
     * login/create account splash screen
     */
    public void userLogout() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the game activity where user can choose the trivia
     * topic and start a new game
     */
    public void startNewGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    /**
     * This is the first screen user should see once they log in
     */
    public void setInitialLoadedFragment() {
        UserUtils.retrieveUserProfileData(getSupportFragmentManager());
//        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_actionbar_items, menu);
        return true;
    }

    /**
     * Method for handling all action bar item clicks
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.setting:
                return true;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
