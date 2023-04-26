package com.example.encryptit.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encryptit.R;
import com.example.encryptit.database.UserDAO;
import com.example.encryptit.cryptography.MD5Hash;
import com.example.encryptit.model.User;

public class SignUpActivity extends AppCompatActivity {


    EditText pass1, pass2, hint;
    Button bt;

    UserDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db = new UserDAO(SignUpActivity.this);

        pass1 = findViewById(R.id.editTextTextPassword);
        pass2 = findViewById(R.id.editTextTextPassword2);
        hint = findViewById(R.id.editTextTextPersonName);
        bt = findViewById(R.id.buttonSignUp);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pass1.getText().toString().matches("") && !pass2.toString().matches("") && !hint.toString().matches("")) {
                    if (pass1.getText().toString().equals(pass2.getText().toString())) {
                        String hashedPass = MD5Hash.md5(pass1.getText().toString());
                        final User user = new User("admin", hashedPass, hint.getText().toString());
                        db.addUser(user);
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Two passwords need to be similar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Please fill all", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}