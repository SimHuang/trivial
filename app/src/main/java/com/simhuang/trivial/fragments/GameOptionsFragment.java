package com.simhuang.trivial.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.R;
import com.simhuang.trivial.async.TriviaNetworkCall;
import com.simhuang.trivial.model.Game;
import com.simhuang.trivial.model.MashapeResults;
import com.simhuang.trivial.service.MashapeService;

import retrofit2.Call;

import static android.content.ContentValues.TAG;

public class GameOptionsFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    private FirebaseUser currentUser;
    private TextView mTopicTextView;
    private Button mStartButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_game_options, container, false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mTopicTextView = view.findViewById(R.id.game_topic_textview);
        mStartButton = view.findViewById(R.id.start_game_button);
        String gameTopic = null;

        Bundle args = getArguments();
        if(args != null) {
            gameTopic = args.getString("topic");
            mTopicTextView.setText(gameTopic);

        }else {
            //return to game topic activity?
        }

        final String gameTopicReference = gameTopic;
        //set the click listener for the start button
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameTopicReference != null) {
                    startGame(gameTopicReference);
                }
            }
        });

        return view;
    }

    /**
     * Start game after user have made all proper selections.
     * Search the database for a avaliable game that matches the user's options,
     * if no game exist a new game object will be created and user will wait for
     * a user to join this game.
     */
    public void startGame(final String gameTopic) {
        int wager = 200;    //read from user input in the future
        //TODO:REQUIRES IMPLMENTATION
        determineGameToJoin(200, gameTopic);
    }

    /**
     * Search for an existing game
     */
    public void determineGameToJoin(final int betAmount, final String topic) {
        Query currentLiveGames = mDatabaseReference.child("games");
//        currentLiveGames.addValueEventListener(new ValueEventListener() {
          //this listener only listens for single event change
          currentLiveGames.addListenerForSingleValueEvent(new ValueEventListener() {

            //search through all current games to find a matching game
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean gameFound = false;
                for(DataSnapshot gameSnapshot: dataSnapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    if(!game.getInProgress()) {
                        if(game.getBetAmount() == betAmount && game.getGameTopic().equals(topic)) {
                            //set the current user as the second player
                            mDatabaseReference.child("games").child(gameSnapshot.getKey()).child("playerTwo").setValue(currentUser.getUid());
                            mDatabaseReference.child("games").child(gameSnapshot.getKey()).child("inProgress").setValue(true);
                            gameFound = true;
                            break;
                        }
                    }
                }

                //no matching games found so create a new one
                if(!gameFound) {
                    createNewGame(topic, betAmount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "determineGamesToJoin:onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Create a new game option and store it into database. This also makes a network
     * request asynchrnously to retrieve questions.
     */
    public void createNewGame(String gameTopic, int betAmount) {
        Game game = new Game(false, currentUser.getUid(), null, gameTopic, betAmount, 0, null, null, null);
        final String key = mDatabaseReference.child("games").push().getKey();

        /*
        * Currently a initial game object gets constructed and pushed into database, after it it successfully
        * stored in db then the questions are retrieved through a network request and push into database as well.
        * This can use some refactoring by retrieving the questions first, construct the game object and push everything
        * into database in one shot.
        * */
        mDatabaseReference.child("games").child(key).setValue(game).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "new game created", Toast.LENGTH_SHORT).show();

                    //pass key into async task for update
                    retrieveTriviaQuestions(key);
                }
            }
        });
    }

    /**
     * retrieve questions through an async task and store questions in database
     */
    public void retrieveTriviaQuestions(String key) {
        MashapeService mashapeService = MashapeService.retrofit.create(MashapeService.class);
        Call<MashapeResults> results = mashapeService.getQuestions(10);
        new TriviaNetworkCall(getContext(), key).execute(results);
    }
}
