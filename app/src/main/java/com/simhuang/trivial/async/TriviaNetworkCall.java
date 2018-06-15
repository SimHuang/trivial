package com.simhuang.trivial.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.model.Game;
import com.simhuang.trivial.model.MashapeQuestion;
import com.simhuang.trivial.model.MashapeResults;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * This class wraps the api call of the trivia api call in an
 * async task so it can be executed in a background thread
 *
 * The three params represent params, progress, and the result that is returned
 */
public class TriviaNetworkCall extends AsyncTask<Call, Void, MashapeResults> {

    private Context context;
    private String gameKey;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public TriviaNetworkCall(Context context, String gameKey) {
        this.context = context;
        this.gameKey = gameKey;
    }

    /**
     * This method will execute the network request in a background thread.
     * @param calls contains only one call from retrofit
     * @return
     */
    @Override
    protected MashapeResults doInBackground(Call... calls) {
        try {
            Call<MashapeResults> call = calls[0];
            Response<MashapeResults> response = call.execute();
            return response.body();

        }catch(IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(MashapeResults mashapeResults) {
        List<MashapeQuestion> triviaQuestions = mashapeResults.getResult();
        databaseReference.child("games").child(gameKey).child("questions").setValue(triviaQuestions);
    }
}
