package com.example.encryptit.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.model.EncryptFile;

import javax.crypto.SecretKey;

public class AddFileToEncrypt {

    Context context;
    FileDAO db;

    public AddFileToEncrypt(Context context, FileDAO db) {
        this.context = context;
        this.db = db;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDb(FileDAO db) {
        this.db = db;
    }

    public void AddSingleFile(Uri uri) {
        EncryptFile encryptFile = new EncryptFile();

        String path = GetPathFromUri.GetPathFromUri(context, uri);
        String nameAndExtension = GetFileName.getFileNameAndExtension(path);
        String name = GetFileName.getFileName(path);
        String extension = GetFileName.getFileExtension(path);
        String location = GetFileName.getFileLocation(path);

        encryptFile.setFilePath(path);
        encryptFile.setFileNameAndExtension(nameAndExtension);
        encryptFile.setFileName(name);
        encryptFile.setFileExtension(extension);
        encryptFile.setFileLocation(location);
        encryptFile.setImage(extension.equals("jpg") || extension.equals("png"));
        encryptFile.setAlias(path);
        Log.d("Tuan", encryptFile.toString());

        db.addFile(encryptFile);

        SecretKey key = MyKeyStore.generateSecretKey(context, path);
        Log.d("My", "onActivityResult: " + key.toString());

        MyEncrypter.encryptFile(path, location + "/" + name + ".encrypt", key);

        Log.d("Tuan", "ma hoa ok");


    }
}
