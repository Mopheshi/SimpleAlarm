package com.github.ppartisan.simplealarms.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.RequiresApi;

import java.util.ArrayList;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.Database;
import com.github.ppartisan.simplealarms.model.Link;
import com.github.ppartisan.simplealarms.model.User;

public class SignupActivity extends AppCompatActivity {
    EditText userName, passWord, confirmPassWord;
    Button signUp;
    TextView signInOption;

    ArrayList<User> users;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userName = findViewById(R.id.username);
        passWord = findViewById(R.id.password);
        confirmPassWord = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.signup);
        signInOption = findViewById(R.id.signInOption);

        database = new Database(this);
        users = database.selectAllUsers();

        // Ask user to grant permissions
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SET_ALARM,
                    Manifest.permission.VIBRATE, Manifest.permission.WAKE_LOCK,}, 10);
        }

        for (User user : users) {
            if (user.getStatus().equals(getString(R.string.online))) {
                startActivity(new Intent(this, MainActivity.class).putExtra("id", user.getUsername()));
                finish();
                break;
            }
        }

        signUp.setOnClickListener(v -> {
            String username = String.valueOf(userName.getText()),
                    password = String.valueOf(passWord.getText()),
                    confirmPassword = String.valueOf(confirmPassWord.getText());
            boolean hasSignedUp = new Link(this).hasSignedUp(username);

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "No field should be empty!",
                        Toast.LENGTH_SHORT).show();
            } else if (!TextUtils.equals(password, confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Password mismatch!", Toast.LENGTH_SHORT).show();
            } else if (hasSignedUp) {
                Toast.makeText(SignupActivity.this, "You already have an account!\nSign in " +
                        "instead...", Toast.LENGTH_SHORT).show();
            } else {
                database.insertUser(new User(0, username, password, getString(R.string.online)));
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            }
        });

        signInOption.setOnClickListener(v ->
                startActivity(new Intent(SignupActivity.this, SigninActivity.class)));
    }
}