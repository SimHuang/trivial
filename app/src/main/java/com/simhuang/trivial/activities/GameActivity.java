package com.simhuang.trivial.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.simhuang.trivial.R;
import com.simhuang.trivial.fragments.GameTopicsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the game activity and it contains three
 * fragments:
 * 1) Game Topic Fragment where user can select from a list of game topics
 * 2) Game options activity where user can specify options such as token wager,
 * 3) main game fragment where the game takes place
 */
public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //TODO: RETRIEVE GAME TOPICS LIST DYNAMICALLY
        ArrayList<String> gameTopics = new ArrayList<String>();
        gameTopics.add("Computer Science");
        gameTopics.add("History");
        gameTopics.add("Movies");
        gameTopics.add("Law");
        gameTopics.add("Biology");
        gameTopics.add("Physics");


        //load the game topics fragment
        GameTopicsFragment gameTopicsFragment = new GameTopicsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("gameTopicsList", gameTopics);
        gameTopicsFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, gameTopicsFragment);
        fragmentTransaction.commit();
    }
}
