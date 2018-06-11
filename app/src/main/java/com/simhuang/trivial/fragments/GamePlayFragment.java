package com.simhuang.trivial.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

public class GamePlayFragment extends Fragment implements View.OnClickListener{

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_play, container, false);

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
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //this listener is to reads all questions and choices in memory from Firebase.
        //The first question is also set in Firebase.
        mDatabase.child("games").child(gameKey).addListenerForSingleValueEvent(new ValueEventListener() {
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

        //This listener is used for the game play
        gamePlayValueEventListenerRef = mDatabase.child("games")
                .child(gameKey)
                .addValueEventListener(gamePlayValueEventListener);

        //TODO: ADD PERMANENT TIMER TO GAME PLAY. THE TIMER SHOULD BE RESET AFTER BOTH PLAYERS SELECTS ANSWERS OR TIME RUNS OUT

        return view;
    }

    /**
     * This is the valueevent listener used for listening to all game play action.
     * When a selects a answer choice the value event listener will be triggered saving
     * the answers.
     */
    ValueEventListener gamePlayValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Game game = dataSnapshot.getValue(Game.class);

            if(game != null) {
                //TODO: CHECK FOR UPDATED VALUE
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

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
        Toast.makeText(getContext(), "you clicked on a button", Toast.LENGTH_SHORT).show();

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
            deteremineWinner();

        }else {
            currentQuestion++;
        }

        changeQuestion(currentQuestion);
    }

    /**
     * Method to determine the winner and save all required data.
     * - compare both players wrong to right to determine winner
     * - set the new token count for both players
     * - delete the game object
     * - set the new win/loss record for both players
     */
    public void deteremineWinner() {
        Toast.makeText(getContext(), "You might have won", Toast.LENGTH_SHORT).show();
        if(isPlayerOne) {
            mDatabase.child("games").child(gameKey).child("playerOneAnswers").setValue(playerAnswers);
        }else {
            mDatabase.child("games").child(gameKey).child("playerOneAnswers").setValue(playerAnswers);
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
        mDatabase.removeEventListener(gamePlayValueEventListenerRef);
    }
}
