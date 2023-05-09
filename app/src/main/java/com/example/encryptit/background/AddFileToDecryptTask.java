package com.example.encryptit.background;

import android.os.AsyncTask;
import android.util.Log;

import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.view.app.MainActivity;
import com.example.encryptit.view.fragment.FileFragment;

import java.util.List;

import javax.crypto.SecretKey;

public class AddFileToDecryptTask extends AsyncTask<EncryptFile, Void, List<EncryptFile>> {
    @Override
    protected List<EncryptFile> doInBackground(EncryptFile... encryptFiles) {
        List<EncryptFile> encryptFilesList = FileFragment.getEncryptFileList();
        for (EncryptFile f : encryptFiles) {
            String path = f.getFilePath();
            String location = f.getFileLocation();
            String name = f.getFileName();
            SecretKey key = MyKeyStore.loadSecretKey(path);
            MyEncrypter.decryptFile(location + "/" + name + ".encrypt", path, key);

            MainActivity.db.deleteFile(f);

            encryptFilesList.remove(f);

            Log.d("AddFileToDecryptTask", "doInBackground: decrypt ok");
        }
        return encryptFilesList;
    }

    @Override
    protected void onPostExecute(List<EncryptFile> files) {
        super.onPostExecute(files);
        FileFragment.setEncryptFileList(files);
    }
}
