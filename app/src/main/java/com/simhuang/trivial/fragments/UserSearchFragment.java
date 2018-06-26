package com.simhuang.trivial.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.R;
import com.simhuang.trivial.model.Friend;
import com.simhuang.trivial.model.UserTag;

import java.util.ArrayList;
import java.util.List;

public class UserSearchFragment extends Fragment {

    private RecyclerView friendSearchRecyclerView;
//    private RecyclerView.Adapter friendSearchAdapter;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<UserTag, ViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference mDatabase;
    private EditText searchEditText;
//    private List<User> userList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_search, container, false);

        //hide the actionbar for this specific activity
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        friendSearchRecyclerView = view.findViewById(R.id.friend_search_recycler_view);
        friendSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchEditText = view.findViewById(R.id.user_search);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserTag");

        /**
         * Search a text change listener to detect when a user types a username
         * in the edit box. We will automatically search the database for a matching user
         */
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getContext(), s.toString(), Toast.LENGTH_SHORT).show();
                Query userQuery = mDatabase.orderByChild("username").startAt(s.toString()).endAt(s.toString());
                FirebaseRecyclerOptions<UserTag> options = new FirebaseRecyclerOptions.Builder<UserTag>()
                        .setQuery(userQuery, UserTag.class)
                        .build();

                firebaseSearch(options);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    /**
     * This performs a firebase search to find
     * @param options
     */
    public void firebaseSearch(FirebaseRecyclerOptions options) {
        //Using the firebase recyclerview adapter to populate the view holder items. This is an
        //external library and we don't have to use a custom adapter for normal recycler views
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserTag, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserTag model) {
                holder.username.setText(model.getUsername());

                final String username = model.getUsername();
                final String uid = model.getUid();
                holder.addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSelectedFriend(username, uid);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_search, parent, false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }
        };

        friendSearchRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    /**
     * add new friend when user clicks on the add friend button
     */
    public void addSelectedFriend(final String username, final String uid) {
        String currentUID = mAuth.getCurrentUser().getUid();
        final DatabaseReference userFriendListRef = FirebaseDatabase.getInstance().getReference().child("User_Friends").child(currentUID);
        userFriendListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Friend> friendsList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Friend friend = snapshot.getValue(Friend.class);
                    friendsList.add(friend);
                }

                Friend friend = new Friend(uid, username);
                boolean friendExist = false;


                for(int i = 0; i < friendsList.size(); i++) {
                    Friend currentFriend = friendsList.get(i);
                    if(currentFriend.getUsername().equals(username)) {
                        friendExist = true;
                        Toast.makeText(getContext(), "User already exist in your friend's list.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if(!friendExist) {
                    friendsList.add(friend);
                    Toast.makeText(getContext(), "Friend added.", Toast.LENGTH_SHORT).show();
                    userFriendListRef.setValue(friendsList);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//        firebaseRecyclerAdapter.stopListening();
    }

    /**
     * Internal view holder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView userAvatar;
        private TextView username;
        private Button addButton;

        /**
         * View Holder constructor
         * @param view represents the entire layout of a single list item
         */
        public ViewHolder(View view) {
            super(view);
            this.userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
            this.username = (TextView) view.findViewById(R.id.username);
            this.addButton = (Button) view.findViewById(R.id.add_button);
        }
    }
}
