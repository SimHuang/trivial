package com.simhuang.trivial.fragments;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simhuang.trivial.R;
import com.simhuang.trivial.activities.GameActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;

public class UserProfileFragment extends Fragment {

    private static final String TOKEN = " Tokens ";
    private static final String WINS = " Wins ";
    private static final String LOSSES = " Losses ";

    private ImageView profileImage;
    private TextView username;
    private TextView tokens;
    private TextView gamesWon;
    private TextView gamesLost;
    private FloatingActionButton fab;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.playgame_fab);
        profileImage = (ImageView) view.findViewById(R.id.profile_image);
        username = (TextView) view.findViewById(R.id.profile_username);
        tokens = (TextView) view.findViewById(R.id.profile_token);
        gamesWon = (TextView) view.findViewById(R.id.profile_gamesWon);
        gamesLost = (TextView) view.findViewById(R.id.profile_gamesLost);
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

//        progressBar.setVisibility(View.VISIBLE);

        Bundle args = getArguments();
        if(args != null) {

            String user = (String) args.get("username");
            int token = (Integer) args.get("token");
            int won = (Integer) args.get("gamesWon");
            int lost = (Integer) args.get("gamesLost");

            username.setText(user);
            tokens.setText(Integer.toString(token));
            gamesWon.setText(Integer.toString(won));
            gamesLost.setText(Integer.toString(lost));

            if(args.containsKey("imageURI")) {
                String imageURL = args.getString("imageURI");
                Picasso.get().load(imageURL).into(profileImage);
            }
        }

        //go to topic selection screen when user clicks on the fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameTopicsScreen();
            }
        });

//        progressBar.setVisibility(View.INVISIBLE);

        return view;
    }

    public void goToGameTopicsScreen() {
        Intent intent = new Intent(getContext(), GameActivity.class);
        startActivity(intent);
    }
}
