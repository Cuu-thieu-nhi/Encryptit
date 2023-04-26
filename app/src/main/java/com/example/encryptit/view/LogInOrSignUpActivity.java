package com.example.encryptit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encryptit.R;
import com.example.encryptit.database.UserDAO;
import com.example.encryptit.model.User;

import java.util.ArrayList;
import java.util.List;

public class LogInOrSignUpActivity extends AppCompatActivity {

    UserDAO db;
    List<User> userArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_or_sign_up);
        db = new UserDAO(LogInOrSignUpActivity.this);
        userArrayList = db.getAllUsers();
        if (userArrayList.isEmpty()) {
            Log.d("Tuan", "empty database");
            Intent intent = new Intent(LogInOrSignUpActivity.this, SignUpActivity.class);
            startActivity(intent);
        } else {
            Log.d("Tuan", "not empty database");
            Intent intent = new Intent(LogInOrSignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}