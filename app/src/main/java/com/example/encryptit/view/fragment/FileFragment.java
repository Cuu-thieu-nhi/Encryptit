package com.example.encryptit.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;
import com.example.encryptit.adapter.FileRecycleViewAdapter;
import com.example.encryptit.background.AddFileToDecryptTask;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.mInterface.IClickFileListener;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.utils.MySort;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class FileFragment extends Fragment {

    public static List<EncryptFile> encryptFileList = new ArrayList<>();
    public static FileRecycleViewAdapter adapter;
    ImageButton bt;
    Spinner spinner;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FileDAO db;
    private FirebaseAuth auth;
    private String email = "";

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getCurrentUserAndEmail();

        Log.d("Email-file", "onCreate: " + email);

        encryptFileList.clear();

        db = new FileDAO(getContext());
        encryptFileList = db.getAllFiles(email);
        Log.d("FileFragment", "onCreate: " + encryptFileList.size());

        for (EncryptFile f : encryptFileList) {
            Log.d("FileFragment", "onCreate: " + f.getFilePath());
        }

        View view = inflater.inflate(R.layout.fragment_file, container, false);
        bt = view.findViewById(R.id.decryptAll);
        progressBar = view.findViewById(R.id.progressBar);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"Sắp xếp mặc định", "Sắp xếp theo tên", "Sắp xếp theo kiểu"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = view.findViewById(R.id.spinner_sort);
        spinner.setAdapter(spinnerAdapter);

        recyclerView = view.findViewById(R.id.recycleViewFile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FileRecycleViewAdapter(encryptFileList, getContext(), new IClickFileListener() {
            @Override
            public void onLongClickImageListener(List<EncryptFile> checkedFiles) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to decrypt these files? (" + checkedFiles.size() + " files)").setTitle("Confirmation").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (EncryptFile f : checkedFiles) {
                            AddFileToDecryptTask task = new AddFileToDecryptTask();
                            task.execute(f);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem.equals("Sắp xếp theo tên")) {
                    List<EncryptFile> sortedList = new ArrayList<>();
                    sortedList.addAll(encryptFileList);
                    MySort.sortByNameAndExtension(sortedList);
                    adapter.setFiles(sortedList);
                } else if (selectedItem.equals("Sắp xếp theo kiểu")) {
                    List<EncryptFile> sortedList = new ArrayList<>();
                    sortedList.addAll(encryptFileList);
                    MySort.sortByExtensionAndName(sortedList);
                    adapter.setFiles(sortedList);
                } else {
                    adapter.setFiles(encryptFileList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.setFiles(encryptFileList);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setEncryptFileList(encryptFileList);
        spinner.setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        encryptFileList.clear();
    }

    public void getCurrentUserAndEmail() {
        email = "";
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            if (email != null) {
                List<? extends UserInfo> providerData = user.getProviderData();
                for (UserInfo userInfo : providerData) {
                    String providerId = userInfo.getProviderId();
                    if (providerId.equals("firebase")) {
                        email += ".firebase";
                    } else if (providerId.equals("google.com")) {
                        email += ".google";
                    } else if (providerId.equals("facebook.com")) {
                        email += ".facebook";
                    }
                }
            }
        }
    }
}

