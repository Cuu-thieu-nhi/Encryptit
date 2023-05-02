package com.example.encryptit.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;
import com.example.encryptit.adapter.FileRecycleViewAdapter;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.model.EncryptFile;

import java.util.ArrayList;
import java.util.List;

public class FileFragment extends Fragment {

    TextView tv;
    RecyclerView recyclerView;

    FileRecycleViewAdapter fileRecycleViewAdapter;
    List<EncryptFile> encryptFileList = new ArrayList<>();
    FileDAO db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new FileDAO(getContext());
        encryptFileList = db.getAllFiles();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fileRecycleViewAdapter = new FileRecycleViewAdapter(encryptFileList, getContext());
        recyclerView.setAdapter(fileRecycleViewAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fileRecycleViewAdapter.setFiles(encryptFileList);
    }
}
