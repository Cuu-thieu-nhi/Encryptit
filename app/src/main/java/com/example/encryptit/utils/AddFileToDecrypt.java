package com.example.encryptit.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.model.EncryptFile;

import javax.crypto.SecretKey;

public class AddFileToDecrypt {

    static Context context;
    static FileDAO db;

    public AddFileToDecrypt(Context context, FileDAO db) {
        this.context = context;
        this.db = db;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDb(FileDAO db) {
        this.db = db;
    }

    public static void addSingleFile(@NonNull EncryptFile f) {
        String path = f.getFilePath();
        String location = f.getFileLocation();
        String name = f.getFileName();
        SecretKey key = MyKeyStore.loadSecretKey(context, path);
        MyEncrypter.decryptFile(location + "/" + name + ".encrypt", path, key);

        db.deleteFile(f);

        Log.d("AddFileToDecrypt", "addSingleFile: decrypt ok");
    }
}
