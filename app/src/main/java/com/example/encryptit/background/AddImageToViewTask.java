package com.example.encryptit.background;

import android.os.AsyncTask;
import android.util.Log;

import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.model.TempImageToView;
import com.example.encryptit.view.fragment.ImageFragment;

import java.util.List;

import javax.crypto.SecretKey;

public class AddImageToViewTask extends AsyncTask <EncryptFile, Void, List<TempImageToView>> {

    @Override
    protected List<TempImageToView> doInBackground(EncryptFile... encryptFiles) {
        List<TempImageToView> tempImageToViews = ImageFragment.getImagesToView();
        TempImageToView t = new TempImageToView();
        t.setFile(encryptFiles[0]);
        String path = encryptFiles[0].getFilePath();
        String location = encryptFiles[0].getFileLocation();
        String name = encryptFiles[0].getFileName();
        SecretKey key = MyKeyStore.loadSecretKey(path);
        t.setData(MyEncrypter.decryptFileToViewTemporary(location + "/" + name + ".encrypt", key));
        tempImageToViews.add(t);
        return tempImageToViews;
    }

    @Override
    protected void onPostExecute(List<TempImageToView> tempImageToViews) {
        super.onPostExecute(tempImageToViews);
        ImageFragment.setImagesToView(tempImageToViews);

        Log.d("AddImageToViewTask", "onPostExecute: add ok");
    }
}
