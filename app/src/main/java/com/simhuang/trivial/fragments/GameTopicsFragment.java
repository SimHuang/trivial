package com.simhuang.trivial.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simhuang.trivial.R;
import com.simhuang.trivial.adapter.GameTopicsAdapter;

import java.util.ArrayList;

/**
 * The game topic fragment which allows user to choose a topic
 * to compete in
 */
public class GameTopicsFragment extends Fragment{

    private RecyclerView mRecyclerView;                     //reference to the recycler view
    private RecyclerView.Adapter mAdapter;                  //retrieving data
    private RecyclerView.LayoutManager mLayoutManager;      //display list linear fashion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_topics, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.game_topics_recyclerView);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //retrieve list of data
        Bundle arguments = getArguments();
        ArrayList<String> gameTopicsList = (ArrayList<String>) arguments.get("gameTopicsList");

        mAdapter = new GameTopicsAdapter(getContext(), gameTopicsList);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
