package com.simhuang.trivial.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.simhuang.trivial.R;
import com.simhuang.trivial.activities.UserHomeActivity;

public class GameFinishFragment extends DialogFragment {

    private Button goHomeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_finish, container, false);

        goHomeButton = (Button) view.findViewById(R.id.go_home_button);
        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserHomeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
