package com.example.encryptit.background;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.utils.GetFileName;
import com.example.encryptit.utils.GetPathFromUri;
import com.example.encryptit.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class AddFileToEncryptTask extends AsyncTask<Uri, Void, List<EncryptFile>> {
    private final Context context;

    public AddFileToEncryptTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<EncryptFile> doInBackground(Uri... uris) {
        List<EncryptFile> encryptFileList = new ArrayList<>();
        for (Uri i : uris) {
            EncryptFile encryptFile = new EncryptFile();

            String path = GetPathFromUri.GetPathFromUri(context, i);
            String nameAndExtension = GetFileName.getFileNameAndExtension(path);
            String name = GetFileName.getFileName(path);
            String extension = GetFileName.getFileExtension(path);
            String location = GetFileName.getFileLocation(path);

            encryptFile.setFilePath(path);
            encryptFile.setFileNameAndExtension(nameAndExtension);
            encryptFile.setFileName(name);
            encryptFile.setFileExtension(extension);
            encryptFile.setFileLocation(location);
            encryptFile.setImage(extension.equals("jpg") || extension.equals("png") || extension.equals("bmp") || extension.equals("webp"));
            encryptFile.setAlias(path);
            Log.d("Tuan", encryptFile.toString());

            MainActivity.db.addFile(encryptFile);

            SecretKey key = MyKeyStore.generateSecretKey(path);
            Log.d("My", "onActivityResult: " + key.toString());

            MyEncrypter.encryptFile(path, location + "/" + name + ".encrypt", key);

            Log.d("Tuan", "ma hoa ok");
            encryptFileList.add(encryptFile);
        }
        return encryptFileList;
    }

    @Override
    protected void onPostExecute(List<EncryptFile> files) {
        Toast.makeText(context, "Đã mã hoá thành công " + files.size() + " file!", Toast.LENGTH_SHORT).show();
        for (EncryptFile f: files) {
            if (f.getImage() == true) {
                new AddImageToViewTask().execute(f);
            }
        }
    }
}
