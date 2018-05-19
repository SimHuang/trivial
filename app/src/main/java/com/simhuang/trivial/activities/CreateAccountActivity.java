package com.simhuang.trivial.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simhuang.trivial.R;
import com.simhuang.trivial.model.User;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button goToLogInButton;
    private Button createAccountButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        goToLogInButton = findViewById(R.id.log_in);
        createAccountButton = findViewById(R.id.create_account);
        emailEditText = findViewById(R.id.email);
        passwordAgainEditText = findViewById(R.id.passwordAgain);
        passwordEditText = findViewById(R.id.password);
        usernameEditText = findViewById(R.id.username);

        goToLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogInActivity();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String password2 = passwordAgainEditText.getText().toString();
                final String username = usernameEditText.getText().toString();

                //get a child node with key "username" and verify account
                if(isAccountInfoVerified(email, password, password2)) {
                    mDatabase.child("Users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Toast.makeText(getApplicationContext(), "Username already exists.", Toast.LENGTH_SHORT).show();

                            }else {
                                //create account when no duplicate username exists
                                createNewAccount(email, password);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //NO IMPLEMENTATION
                        }
                    });


                }else {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null) {
//            //TODO:GO TO MAIN USER ACTIVITY
//        }
//    }

    /**
     * Go to login activity
     */
    public void goToLogInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Save the username and go to user home screen after user have been authenticated
     */
    public void saveUserAndGoHome() {
        String username = usernameEditText.getText().toString();
        int token = 1000;
        User user = new User(username, emailEditText.getText().toString(), null, null, null,token,0,0,0);
        mDatabase.child("Users").child(username).setValue(user);

        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }

    /**
     * Login containing all verification for account input
     * @return
     */
    public boolean isAccountInfoVerified(String email, String password, String password2) {
        if(!password.equals(password2)) {
            return false;
        }

        return true;
    }

    /**
     * Create New User account
     */
    public void createNewAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Account Successfully created", Toast.LENGTH_SHORT).show();
                            saveUserAndGoHome();

                        }else {
                            //TODO: REFACTOR - determine exceptions that will be displayed
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
