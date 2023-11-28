package com.github.ppartisan.simplealarms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.Database;
import com.github.ppartisan.simplealarms.model.User;

public class SigninActivity extends AppCompatActivity {
    EditText userName, passWord;
    Button signIn;

    ArrayList<User> users;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        userName = findViewById(R.id.username);
        passWord = findViewById(R.id.password);
        signIn = findViewById(R.id.signin);

        database = new Database(this);
        users = database.selectAllUsers();

        signIn.setOnClickListener(v -> {
            for (User user : users) {
                String dbUserName = user.getUsername(), pin = user.getPassword(),
                        username = String.valueOf(userName.getText()), password = String.valueOf(passWord.getText());

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "No field should be empty!", Toast.LENGTH_SHORT).show();
                    break;
                } else if (dbUserName.equals(username) && pin.equals(password)) {
                    database.updateStatus(username, getString(R.string.online));
                    startActivity(new Intent(SigninActivity.this, MainActivity.class));
                    break;
                } else {
                    Toast.makeText(this, "Sign in credentials don't match!", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });
    }
}