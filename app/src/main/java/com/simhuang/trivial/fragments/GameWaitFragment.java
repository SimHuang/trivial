package com.simhuang.trivial.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.R;
import com.simhuang.trivial.model.Game;

/**
 * This fragment just displays a nice loading screen
 * while user waits for game to start
 */
public class GameWaitFragment extends Fragment{

    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListener;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_wait, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mContext = getContext();

        final String gameKey = getArguments().getString("gameKey");

        /*
         * Add a listener to detect when player two has join. This listener is for device one which
         * was responsible for creating the game object and waiting for player two.
         * */
        valueEventListener = mDatabase.child("games").child(gameKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);

                if (game != null) {
                    if(game.getPlayerTwo() != null) {
                        GamePlayFragment gamePlayFragment = new GamePlayFragment();
                        Bundle args = new Bundle();
                        args.putString("gameKey", gameKey);
                        gamePlayFragment.setArguments(args);

                        FragmentTransaction fragmentTransaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                        //TODO: WHEN SHOULD I USE getChildFragmentManager vs getSupportFragmentManager
//                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, gamePlayFragment);
                        fragmentTransaction.commit();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO:REQUIRES IMPLEMENTATION
            }
        });


        return view;
    }

    /**
     * This life cycle method is called when user clicks back button or
     * the fragment is no longer visible.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDatabase.removeEventListener(valueEventListener);
    }
}
