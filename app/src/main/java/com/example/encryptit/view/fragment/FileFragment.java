package com.example.encryptit.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;
import com.example.encryptit.adapter.FileRecycleViewAdapter;
import com.example.encryptit.background.AddFileToDecryptTask;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.model.EncryptFile;

import java.util.ArrayList;
import java.util.List;

public class FileFragment extends Fragment {

    public static List<EncryptFile> encryptFileList = new ArrayList<>();
    public static FileRecycleViewAdapter adapter;
    ImageButton bt;
    RecyclerView recyclerView;
    FileDAO db;

    public static List<EncryptFile> getEncryptFileList() {
        return encryptFileList;
    }

    public static void setEncryptFileList(List<EncryptFile> encryptFileList) {
        FileFragment.encryptFileList = encryptFileList;
        adapter.setFiles(encryptFileList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new FileDAO(getContext());
        encryptFileList = db.getAllFiles();
        Log.d("FileFragment", "onCreate: " + encryptFileList.size());

        for (EncryptFile f: encryptFileList) {
            Log.d("FileFragment", "onCreate: " + f.getFilePath());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        bt = view.findViewById(R.id.decryptAll);
        recyclerView = view.findViewById(R.id.recycleViewFile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FileRecycleViewAdapter(encryptFileList, getContext());
        recyclerView.setAdapter(adapter);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EncryptFile f : encryptFileList) {
                    AddFileToDecryptTask task = new AddFileToDecryptTask();
                    task.execute(f);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setEncryptFileList(encryptFileList);
    }
}
