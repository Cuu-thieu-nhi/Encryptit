package com.example.encryptit.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.encryptit.R;
import com.example.encryptit.cryptography.MD5Hash;
import com.example.encryptit.database.UserDAO;
import com.example.encryptit.model.User;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldEdt, newEdt, confirmEdt;
    Button bt;
    UserDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        oldEdt = findViewById(R.id.editTextOld);
        newEdt = findViewById(R.id.editTextNew);
        confirmEdt = findViewById(R.id.editTextConfirm);
        bt = findViewById(R.id.buttonChangePass);

        db = new UserDAO(ChangePasswordActivity.this);
        User user = db.getUser(1);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(oldEdt.getText().toString()) || TextUtils.isEmpty(newEdt.getText().toString()) || TextUtils.isEmpty(confirmEdt.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Please complete all information", Toast.LENGTH_SHORT).show();
                } else if (!newEdt.getText().toString().equals(confirmEdt.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Confirmation password is incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    String hashedPass = MD5Hash.md5(oldEdt.getText().toString());
                    if (hashedPass.equals(user.getPassWord())) {
                        String newHashedPass = MD5Hash.md5(newEdt.getText().toString());
                        final User newUser = new User("admin", newHashedPass, user.getHint());
                        db.updateUser(1, newUser);
                        Toast.makeText(ChangePasswordActivity.this, "Change password successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}