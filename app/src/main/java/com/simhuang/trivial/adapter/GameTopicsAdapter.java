package com.simhuang.trivial.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.simhuang.trivial.R;
import com.simhuang.trivial.fragments.GameOptionsFragment;

import java.util.List;

/**
 * This adapter is responsible for passing data to game topics recycler view
 */
public class GameTopicsAdapter extends RecyclerView.Adapter<GameTopicsAdapter.ViewHolder> {

    private List<String> gameTopics;
    private Context mContext;

    /**
     * Static class for storing views in a view holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            this.textView = view.findViewById(R.id.gameTopic_name);
        }
    }


    /**
     * Adapter class constructor
     * @param context the current view context of the activity
     * @param gameTopics list of game topics being passed from the gametopics fragment
     */
    public GameTopicsAdapter(Context context, List<String> gameTopics) {
        this.gameTopics = gameTopics;
        mContext = context;
    }

    /**
     * Method for view construction. This creates a viewholder for each
     * list item. The view holder is the "card" that user sees and they can
     * click on for further action. This method also contains the onclicklistener
     * for when a user clicks on one of the view holder.
     */
    @NonNull
    @Override
    public GameTopicsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game_topics, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        //set the on click listener
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = viewHolder.textView.getText().toString();
                Toast.makeText(mContext, topic, Toast.LENGTH_SHORT).show();

                GameOptionsFragment gameOptionsFragment = new GameOptionsFragment();
                Bundle args = new Bundle();
                args.putString("topic", topic);
                gameOptionsFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, gameOptionsFragment);
                fragmentTransaction.commit();
            }
        });

        return viewHolder;
    }

    /**
     * Method for data binding The holder UI element binds to the data base on the coressponding
     * position
     * @param holder object storing the UI element
     * @param position the position of the element in the list
     */
    @Override
    public void onBindViewHolder(@NonNull GameTopicsAdapter.ViewHolder holder, int position) {
        holder.textView.setText(gameTopics.get(position));
    }

    @Override
    public int getItemCount() {
        return gameTopics.size();
//        return 0;
    }
}
