package com.example.encryptit.view.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.encryptit.R;
import com.example.encryptit.background.AddImageToDecryptTask;
import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.model.TempImageToView;

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

        String location = image.getFileLocation();
        String name = image.getFileName();

        SecretKey key = MyKeyStore.loadSecretKey(image.getFilePath());
        byte[] data = MyEncrypter.decryptFileToViewTemporary(location + "/" + name + ".encrypt", key);
        Glide.with(ImageViewActivity.this).load(data).into(imageView);

        btDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TempImageToView t = new TempImageToView();
                t.setFile(image);
//                t.setData(data);
                AddImageToDecryptTask task = new AddImageToDecryptTask(ImageViewActivity.this);
                task.execute(t);
            }
        });
    }
}