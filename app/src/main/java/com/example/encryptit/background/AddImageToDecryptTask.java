package com.example.encryptit.background;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.model.TempImageToView;
import com.example.encryptit.view.MainActivity;
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
        for (TempImageToView f: TempImageToViews) {
            String path = f.getFile().getFilePath();
            String location = f.getFile().getFileLocation();
            String name = f.getFile().getFileName();
            SecretKey key = MyKeyStore.loadSecretKey(path);
            MyEncrypter.decryptFile(location + "/" + name + ".encrypt", path, key);
            MainActivity.db.deleteFile(f.getFile());
            t.remove(f);
            Log.d("AddImageToDecryptTask", "doInBackground: decrypt ok");
        }
        return t;
    }

    @Override
    protected void onPostExecute(List<TempImageToView> t) {
        ImageFragment.setImagesToView(t);
        Activity activity = (Activity) context;
        if (activity instanceof MainActivity) {
            ImageFragment imageFragment = (ImageFragment) ((MainActivity) activity).getSupportFragmentManager().findFragmentByTag("ImageFragment");
            if (imageFragment != null) {
            } else {
                activity.finish();
            }
        }
    }
}
