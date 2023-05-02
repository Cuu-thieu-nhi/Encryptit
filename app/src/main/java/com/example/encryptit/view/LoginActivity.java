package com.example.encryptit.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encryptit.R;
import com.example.encryptit.cryptography.MD5Hash;
import com.example.encryptit.database.UserDAO;
import com.example.encryptit.model.User;

public class LoginActivity extends AppCompatActivity {
    UserDAO db;
    EditText pass;
    Button bt, bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new UserDAO(LoginActivity.this);

        pass = findViewById(R.id.editTextPassword3);
        bt = findViewById(R.id.buttonLogin);
        bt1 = findViewById(R.id.buttonForgot);

        User user = db.getUser(1);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString() != null) {
                    // lấy mã md5 của mật khẩu đã nhập, nếu trùng với mã nd5 được lưu trong cơ sở dữ liệu thì có nghĩa là đúng mật khẩu
                    String hashedPass = MD5Hash.md5(pass.getText().toString());
                    if (hashedPass.equals(user.getPassWord())) {
                        Log.d("Tuan", "equal");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Your password hint is " + user.getHint(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}