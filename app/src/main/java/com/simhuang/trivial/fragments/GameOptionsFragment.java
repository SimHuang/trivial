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

import com.simhuang.trivial.R;

public class GameOptionsFragment extends Fragment {

    private TextView mTopicTextView;
    private Button mStartButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_game_options, container, false);

        mTopicTextView = view.findViewById(R.id.game_topic_textview);
        mStartButton = view.findViewById(R.id.start_game_button);

        Bundle args = getArguments();
        if(args != null) {
            String gameTopic = args.getString("topic");
            mTopicTextView.setText(gameTopic);

        }else {
            //return to game topic activity?
        }

        //set the click listener for the start button
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        return view;
    }

    /**
     * Start game after user have made all proper selections
     */
    public void startGame() {
        int wager = 200;
        //TODO:REQUIRES IMPLMENTATION
    }
}
