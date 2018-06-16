package com.simhuang.trivial.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.R;
import com.simhuang.trivial.model.Game;
import com.simhuang.trivial.model.MashapeQuestion;
import com.simhuang.trivial.model.User;

import java.util.ArrayList;
import java.util.List;

public class GamePlayFragment extends Fragment implements View.OnClickListener{

    private static DatabaseReference winnerRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private static DatabaseReference loserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private ValueEventListener gamePlayValueEventListenerRef;
    private boolean isPlayerOne;
    private int correctAnswers;
    private List<MashapeQuestion> questionsList;
    private List<Boolean> playerAnswers;
    private String gameKey;
    private int currentQuestion;
    private TextView timer;
    private TextView question;
    private Button choiceOneBtn;
    private Button choiceTwoBtn;
    private Button choiceThreeBtn;
    private Button choiceFourBtn;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_play, container, false);
        Log.d("onCreateView", "Creating the onCreateView");

        timer = view.findViewById(R.id.timer);
        question = (TextView) view.findViewById(R.id.current_question);
        choiceOneBtn = (Button) view.findViewById(R.id.choice_one);
        choiceTwoBtn = (Button) view.findViewById(R.id.choice_two);
        choiceThreeBtn = (Button) view.findViewById(R.id.choice_three);
        choiceFourBtn = (Button) view.findViewById(R.id.choice_four);

        choiceOneBtn.setOnClickListener(this);
        choiceTwoBtn.setOnClickListener(this);
        choiceThreeBtn.setOnClickListener(this);
        choiceFourBtn.setOnClickListener(this);

        Bundle args = getArguments();
        gameKey = args.getString("gameKey");
        playerAnswers = new ArrayList<>();
        currentQuestion = 0;
        isPlayerOne = false;
        correctAnswers = 0;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("games").child(gameKey);
        mContext = getContext();



        //this listener is to reads all questions and choices in memory from Firebase.
        //The first question is also set in Firebase.
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);

                if(game != null) {
                    //determine player one
                    if(game.getPlayerOne().equals(firebaseUser.getUid())) {
                        isPlayerOne = true;
                    }

                    questionsList = game.getQuestions();
                    changeQuestion(currentQuestion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //IGNORE
            }
        });

        //This listener is used to determine when the game is complete and it will calculate who the winner is
        gamePlayValueEventListenerRef = mDatabase.addValueEventListener(gamePlayValueEventListener);

        //TODO: ADD PERMANENT TIMER TO GAME PLAY. THE TIMER SHOULD BE RESET AFTER BOTH PLAYERS SELECTS ANSWERS OR TIME RUNS OUT

        return view;
    }

    /**
     * This is the value event listener used for listening to all game play action.
     * When a selects a answer choice the value event listener will be triggered saving
     * the answers.
     *
     * - compare both players wrong to right to determine winner
     * - set the new token count for both players
     * - delete the game object
     * - set the new win/loss record for both players
     */
    ValueEventListener gamePlayValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Game game = dataSnapshot.getValue(Game.class);

            if(game != null) {
                //both players have completed the game and results are uploaded
                if(game.getPlayerOneAnswers() != null && game.getPlayerTwoAnswers() != null) {
                    int playerOneCorrectTally = 0;
                    int playerTwoCorrectTally = 0;

                    String playerOne = game.getPlayerOne();
                    String playerTwo = game.getPlayerTwo();
                    int betAmount = game.getBetAmount();

                    List<Boolean> playerOneAnswers = game.getPlayerOneAnswers();
                    List<Boolean> playerTwoAnswers = game.getPlayerTwoAnswers();

                    for(int i = 0; i < playerOneAnswers.size(); i++) {
                        if(playerOneAnswers.get(i)) {
                            playerOneCorrectTally++;
                        }

                        if(playerTwoAnswers.get(i)) {
                            playerTwoCorrectTally++;
                        }
                    }

                    updatePlayerResults(playerOneCorrectTally, playerTwoCorrectTally, playerOne, playerTwo, betAmount);

                    deleteFinishedGame();

                    mDatabase.removeEventListener(this);


                    goToGameFinishFragment(playerOneCorrectTally, playerTwoCorrectTally);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //DOES NOT REQUIRE IMPLEMENTATION
        }
    };

    /**
     * Store players new token count and games won/loss in db
     */
    public void updatePlayerResults(int playerOneTally, int playerTwoTally, String playerOne, String playerTwo, final int betAmount) {

        //the winnerRef and loserRef are global
        if(playerOneTally == playerTwoTally) {  //tie game
            return;

        }else if(playerOneTally > playerTwoTally) {
            winnerRef = winnerRef.child(playerOne);
            loserRef = loserRef.child(playerTwo);

        }else {
            winnerRef = winnerRef.child(playerTwo);
            loserRef = loserRef.child(playerOne);
        }

        //listener to update winning player
        winnerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User player = dataSnapshot.getValue(User.class);

                if(player != null) {
                    int newToken = player.getToken() + betAmount;
                    int gamesWon = player.getGamesWon() + 1;
                    winnerRef.child("token").setValue(newToken);
                    winnerRef.child("gamesWon").setValue(gamesWon);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //DOES NOT REQUIRE IMPLEMENTATION
            }
        });

        //listener to update for losing player
        loserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User player = dataSnapshot.getValue(User.class);

                if(player != null) {
                    if(player.getToken() != 0) {
                        int newToken = player.getToken() - betAmount;
                        loserRef.child("token").setValue(newToken);
                    }

                    int gamesLost = player.getGamesLost() + 1;
                    loserRef.child("gamesLost").setValue(gamesLost);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //DOES NOT REQUIRE IMPLEMENTATION
            }
        });

    }

    /**
     * Delete the game that is finished
     */
    public void deleteFinishedGame() {
        mDatabase.removeValue();
    }

    /**
     *
     */
    public void goToGameFinishFragment(int playerOneCorrectTally, int playerTwoCorrectTally) {
        GameFinishFragment gameFinishFragment = new GameFinishFragment();
        Bundle args = new Bundle();
        args.putInt("playerOneAnswersTally", playerOneCorrectTally);
        args.putInt("playerTwoAnswersTally", playerTwoCorrectTally);
        args.putBoolean("isPlayerone", isPlayerOne);
        gameFinishFragment.setArguments(args);

        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        gameFinishFragment.show(fragmentManager, "gameFinshDialog");

//        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, gameFinishFragment);
//        fragmentTransaction.commit();
    }

    /**
     * Change questions when both user has selected an answer choice
     * or when the timer is up. Update the UI with the new question.
     */
    public void changeQuestion(int currentQuestion) {
        if(currentQuestion <= 9) {
            MashapeQuestion newQuestion = questionsList.get(currentQuestion);
            question.setText(newQuestion.getQuestion());
            choiceOneBtn.setText(newQuestion.getChooices().get(0));
            choiceTwoBtn.setText(newQuestion.getChooices().get(1));

            if(newQuestion.getChooices().size() > 2) {
                choiceThreeBtn.setText(newQuestion.getChooices().get(2));
                choiceFourBtn.setText(newQuestion.getChooices().get(3));
                choiceThreeBtn.setVisibility(View.VISIBLE);
                choiceFourBtn.setVisibility(View.VISIBLE);

            }else {
                choiceThreeBtn.setVisibility(View.GONE);
                choiceFourBtn.setVisibility(View.GONE);
            }

            enableButtonClicks();
        }
    }

    /**
     * This is the onclick listener method to detect when user clicks on a answer. The
     * user that clicked on an answer will update firebase with their new answer
     * @param v the view that was clicked on
     */
    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.choice_one:
                handlerAnswerSelection(v);
                break;

            case R.id.choice_two:
                handlerAnswerSelection(v);
                break;

            case R.id.choice_three:
                handlerAnswerSelection(v);
                break;

            case R.id.choice_four:
                handlerAnswerSelection(v);
                break;

            default:
                //NOTHING TO DO HERE
                break;
        }
    }

    /**
     * Method to display the next question once a user has selected an answer
     * @param v
     */
    public void handlerAnswerSelection(View v) {
        disableButtonClick();
        String userAnswer = ((Button)v).getText().toString();
        String realAnswer = questionsList.get(currentQuestion).getAnswer();
        if(userAnswer.equals(realAnswer)) {
            playerAnswers.add(currentQuestion, true);
            correctAnswers++;

        }else {
            playerAnswers.add(currentQuestion, false);
        }

        if(currentQuestion == 9) {
            determineWinner();

        }else {
            currentQuestion++;
            changeQuestion(currentQuestion);
        }
    }

    /**
     * Method to determine the winner and save all required data.
     */
    public void determineWinner() {
        if(isPlayerOne) {
            mDatabase.child("playerOneAnswers").setValue(playerAnswers, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null) {
                        Log.d("GamePlay:P1saveResults", "Error saving same results");
                    }
                }
            });

        }else {
            mDatabase.child("playerTwoAnswers").setValue(playerAnswers, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null) {
                        Log.d("GamePlay:P2saveResults", "Error saving same results");
                    }
                }
            });
        }
    }

    /**
     * Disable all button clicks
     */
    public void disableButtonClick() {
        choiceOneBtn.setClickable(false);
        choiceTwoBtn.setClickable(false);
        choiceThreeBtn.setClickable(false);
        choiceFourBtn.setClickable(false);
    }

    /**
     * Enable all button clicks
     */
    public void enableButtonClicks() {
        choiceOneBtn.setClickable(true);
        choiceTwoBtn.setClickable(true);
        choiceThreeBtn.setClickable(true);
        choiceFourBtn.setClickable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mDatabase.removeEventListener(gamePlayValueEventListenerRef);
    }
}
