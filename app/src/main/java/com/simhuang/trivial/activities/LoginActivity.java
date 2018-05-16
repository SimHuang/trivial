package com.simhuang.trivial.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.simhuang.trivial.R;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button createNewAccount;
    private EditText userNameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        login = findViewById(R.id.login);
        createNewAccount = findViewById(R.id.new_account);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateAccountActivity();
            }
        });
    }

    /**
     * Go to user activity if authenticated else
     * display an error message
     */
    public void authenticateUser() {
        //TODO: REQUIRE AUTHENTICATION IMPLEMENTATION
    }

    /**
     * Go to create account activity
     */
    public void goToCreateAccountActivity() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
}
