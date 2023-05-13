package com.example.encryptit.view.account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.encryptit.R;
import com.example.encryptit.cryptography.DatabasePasswordManager;

public class FirstActivity extends AppCompatActivity {
    public final String TAG = "FirstActivity";

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_main);

        try {
            Log.d("Tuan", DatabasePasswordManager.encrypt("kieuthilanhuong"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (isExternalStorageWritable()) {
            Log.d(TAG, "onCreate: ok");
            ActivityCompat.requestPermissions(FirstActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(FirstActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }


}