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
import com.google.firebase.auth.FirebaseUser;
import com.simhuang.trivial.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button login;
    private Button createNewAccount;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        login = findViewById(R.id.login);
        createNewAccount = findViewById(R.id.new_account);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                authenticateUser(email, password);
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateAccountActivity();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            goToHomeActivity(currentUser);
        }
    }

    /**
     * Go to user activity if authenticated else
     * display an error message
     */
    public void authenticateUser(String email, String password) {
        if(!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                goToHomeActivity(currentUser);

                            }else {
                                Toast.makeText(getApplicationContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {
            Toast.makeText(this, "Please fill in username and password field.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToHomeActivity(FirebaseUser user) {
        Intent intent = new Intent(this, UserHomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /**
     * Go to create account activity
     */
    public void goToCreateAccountActivity() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
}
