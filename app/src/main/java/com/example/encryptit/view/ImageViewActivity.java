package com.example.encryptit.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.encryptit.R;
import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.utils.AddFileToDecrypt;

import javax.crypto.SecretKey;

public class ImageViewActivity extends AppCompatActivity {

    TextView tv5;
    Button btDec;
    ImageView imageView;
    FileDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        EncryptFile image = (EncryptFile) getIntent().getSerializableExtra("object_image");

        db = new FileDAO(ImageViewActivity.this);

        tv5 = findViewById(R.id.textView5);
        btDec = findViewById(R.id.decrypt);
        imageView = findViewById(R.id.imageViewww);

        tv5.setText(image.getFileLocation() + "/" + image.getFileName() + ".encrypted");

        String path = image.getFilePath();
        String location = image.getFileLocation();
        String name = image.getFileName();
        SecretKey key = MyKeyStore.loadSecretKey(ImageViewActivity.this, image.getFilePath());
        Glide.with(ImageViewActivity.this).load(MyEncrypter.decryptFileToViewTemporary(location + "/" + name + ".encrypt", key)).into(imageView);

        btDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFileToDecrypt.addSingleFile(image);
                finish();
            }
        });
    }
}