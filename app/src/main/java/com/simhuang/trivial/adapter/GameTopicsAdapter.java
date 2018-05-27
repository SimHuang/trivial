package com.simhuang.trivial.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simhuang.trivial.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * This adapter is responsible for passing data to game topics recycler view
 */
public class GameTopicsAdapter extends RecyclerView.Adapter<GameTopicsAdapter.ViewHolder> {

    private List<String> gameTopics;

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
     */
    public GameTopicsAdapter(List<String> gameTopics) {
        this.gameTopics = gameTopics;
    }

    /**
     * Method for view construction
     */
    @NonNull
    @Override
    public GameTopicsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game_topics, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
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
