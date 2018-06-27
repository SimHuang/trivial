package com.simhuang.trivial.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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
        gameTopics.add("Biology");
        gameTopics.add("Computer Science");
        gameTopics.add("History");
        gameTopics.add("Movies");
        gameTopics.add("Physics");
        gameTopics.add("Random");
        gameTopics.add("Statistics");

        //load the game topics fragment
        GameTopicsFragment gameTopicsFragment = new GameTopicsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("gameTopicsList", gameTopics);
        gameTopicsFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, gameTopicsFragment);
        fragmentTransaction.commit();
    }

    /**
     * Detect when the user clicks the back button
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this, "you pressed the back button", Toast.LENGTH_SHORT).show();

        //check which fragment the user is on and act accordingly
    }
}
