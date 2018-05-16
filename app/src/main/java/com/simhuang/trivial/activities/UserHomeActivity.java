package com.simhuang.trivial.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.simhuang.trivial.R;

/**
 * This is the activity user sees when they are logged in.
 * The main UI with display a list of friends avaliable for
 * them to compete against. There will also be a navigation drawer
 * containing various settings and item clicks.
 */
public class UserHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
    }
}
