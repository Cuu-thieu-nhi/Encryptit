package com.example.encryptit.background;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.model.TempImageToView;
import com.example.encryptit.view.app.ImageViewActivity;
import com.example.encryptit.view.app.MainActivity;
import com.example.encryptit.view.fragment.ImageFragment;

import java.util.List;

import javax.crypto.SecretKey;

public class AddImageToDecryptTask extends AsyncTask<TempImageToView, Void, List<TempImageToView>> {

    private final Context context;

    public AddImageToDecryptTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<TempImageToView> doInBackground(TempImageToView... TempImageToViews) {
        List<TempImageToView> t = ImageFragment.getImagesToView();
        for (TempImageToView f : TempImageToViews) {
            String path = f.getFile().getFilePath();
            Log.d("AddImageToDecryptTask", "doInBackground: " + path);
            String location = f.getFile().getFileLocation();
            String name = f.getFile().getFileName();
            SecretKey key = MyKeyStore.loadSecretKey(path);
            MyEncrypter.decryptFile(location + "/" + name + ".encrypt", path, key);

            MainActivity.db.deleteFile(f.getFile());
            if (t.contains(f)) {
                Log.d("AddImageToDecryptTask", "doInBackground: delete ok");
                t.remove(f);
            } else {
                for (int i = 0; i < t.size(); i++) {
                    if (t.get(i).getFile().getFileName().equals(f.getFile().getFileName())) {
                        t.remove(i);
                        break;
                    }
                }
            }

            Log.d("AddImageToDecryptTask", "doInBackground: decrypt ok");
        }
        return t;
    }

    @Override
    protected void onPostExecute(List<TempImageToView> t) {
        ImageFragment.setImagesToView(t);
        Activity activity = (Activity) context;
        if (activity instanceof ImageViewActivity) activity.finish();
    }
}
