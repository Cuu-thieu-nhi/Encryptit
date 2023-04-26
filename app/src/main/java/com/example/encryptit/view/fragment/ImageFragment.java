package com.example.encryptit.view.fragment;

import static com.example.encryptit.utils.AddFileToDecrypt.addSingleFile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;
import com.example.encryptit.adapter.RecycleViewAdapter;
import com.example.encryptit.cryptography.MyEncrypter;
import com.example.encryptit.cryptography.MyKeyStore;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.model.TempFileToView;
import com.example.encryptit.utils.AddFileToDecrypt;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class ImageFragment extends Fragment {

    RecyclerView recyclerView;
    Button buttonDecryptAll;
    TextView textView;
    List<EncryptFile> encryptedImages = new ArrayList<>();
    List<TempFileToView> imagesToView = new ArrayList<>();
    RecycleViewAdapter adapter;
    FileDAO db;
    AddFileToDecrypt addFileToDecrypt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new FileDAO(getContext());
        addFileToDecrypt = new AddFileToDecrypt(getContext(), db);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycleViewImageGallery);
        textView = view.findViewById(R.id.totalPhoto);
        buttonDecryptAll = view.findViewById(R.id.decryptAll);

        encryptedImages = db.getAllImages();


        if (encryptedImages != null) {
            textView.setText("Photo (" + encryptedImages.size() + ")");
        }

        for (EncryptFile f : encryptedImages) {
            TempFileToView t = new TempFileToView();
            t.setFile(f);
            String path = f.getFilePath();
            String location = f.getFileLocation();
            String name = f.getFileName();
            SecretKey key = MyKeyStore.loadSecretKey(getContext(), path);
            t.setData(MyEncrypter.decryptFileToViewTemporary(location + "/" + name + ".encrypt", key));
            imagesToView.add(t);
        }

        adapter = new RecycleViewAdapter(imagesToView, getContext());

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(adapter);

        buttonDecryptAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                for (EncryptFile f : encryptedImages) {
                    addSingleFile(f);
                    i++;

                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        encryptedImages = db.getAllImages();
        imagesToView.clear();
        for (EncryptFile f : encryptedImages) {
            TempFileToView t = new TempFileToView();
            t.setFile(f);
            String path = f.getFilePath();
            String location = f.getFileLocation();
            String name = f.getFileName();
            SecretKey key = MyKeyStore.loadSecretKey(getContext(), path);
            t.setData(MyEncrypter.decryptFileToViewTemporary(location + "/" + name + ".encrypt", key));
            imagesToView.add(t);
        }
        adapter.setDecryptedImages(imagesToView);
    }
}
