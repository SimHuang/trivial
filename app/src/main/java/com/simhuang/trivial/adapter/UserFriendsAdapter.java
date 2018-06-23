package com.simhuang.trivial.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.simhuang.trivial.R;
import com.simhuang.trivial.model.Friend;

import java.util.List;

public class UserFriendsAdapter extends RecyclerView.Adapter<UserFriendsAdapter.ViewHolder> {

    private List<Friend> friendsList;
    private Context mContext;

    public UserFriendsAdapter(List<Friend> friendsList, Context mContext) {
        this.friendsList = friendsList;
    }

    //Provides a reference to each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private Button playButton;
        private TextView username;

        public ViewHolder (View view) {
            super(view);
            this.imageView = view.findViewById(R.id.user_avatar);
            this.playButton = view.findViewById(R.id.play_button);
            this.username = view.findViewById(R.id.username);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(friendsList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
