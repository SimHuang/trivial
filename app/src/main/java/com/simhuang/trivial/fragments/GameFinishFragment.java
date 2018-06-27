package com.simhuang.trivial.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simhuang.trivial.R;
import com.simhuang.trivial.activities.UserHomeActivity;

public class GameFinishFragment extends DialogFragment {

    private Button goHomeButton;
    private TextView finishTextView;
    private ImageView finishImageview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_finish, container, false);

        finishTextView = view.findViewById(R.id.finish_textview);
        finishImageview = view.findViewById(R.id.finish_image);
        goHomeButton = (Button) view.findViewById(R.id.go_home_button);
        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserHomeActivity.class);
                startActivity(intent);
            }
        });

        Bundle args = getArguments();
        boolean isPlayerOne = args.getBoolean("isPlayerone");
        int playerOneTally = args.getInt("playerOneAnswersTally");
        int playerTwoTally = args.getInt("playerTwoAnswersTally");

        Toast.makeText(getContext(), Integer.toString(playerOneTally) + " " + Integer.toString(playerTwoTally), Toast.LENGTH_SHORT).show();

        //game is tied
        if(playerOneTally == playerTwoTally) {  //tie game
            finishTextView.setText("Tied game!");
            finishImageview.setImageResource(R.drawable.happy_face);

        //player one wins
        }else if(playerOneTally > playerTwoTally) {
            if(isPlayerOne) {
                finishTextView.setText("Congratulations! You won!");
                finishImageview.setImageResource(R.drawable.happy_face);

            }else {
                finishTextView.setText("Sorry! You Lost.");
                finishImageview.setImageResource(R.drawable.sad_face);
            }

        //player two win
        }else {
            if(isPlayerOne) {
                finishTextView.setText("Sorry! You Lost.");
                finishImageview.setImageResource(R.drawable.sad_face);

            }else {
                finishTextView.setText("Congratulations! You won!");
                finishImageview.setImageResource(R.drawable.happy_face);
            }
        }

        return view;
    }
}
