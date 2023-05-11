package com.example.encryptit.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;
import com.example.encryptit.adapter.ImageRecycleViewAdapter;
import com.example.encryptit.background.AddImageToDecryptTask;
import com.example.encryptit.background.AddImageToViewTask;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.mInterface.IClickImageListener;
import com.example.encryptit.model.EncryptFile;
import com.example.encryptit.model.TempImageToView;
import com.example.encryptit.view.app.ImageViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment {

    public static List<EncryptFile> encryptedImages = new ArrayList<>();
    public static List<TempImageToView> imagesToView = new ArrayList<>();
    public static ImageRecycleViewAdapter adapter;
    FileDAO imageDb;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ImageButton buttonDecryptAll;
    private FirebaseAuth auth;
    private String email = "";

    public static List<EncryptFile> getEncryptedImages() {
        return encryptedImages;
    }

    public static void setEncryptedImages(List<EncryptFile> encryptedImages) {
        ImageFragment.encryptedImages = encryptedImages;
    }

    public static List<TempImageToView> getImagesToView() {
        return imagesToView;
    }

    public static void setImagesToView(List<TempImageToView> imagesToView) {
        ImageFragment.imagesToView = imagesToView;
        adapter.setDecryptedImages(imagesToView);
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

        Log.d("Email-image", "onCreate: " + email);

        encryptedImages.clear();
        imagesToView.clear();

        imageDb = new FileDAO(getContext());
        encryptedImages = imageDb.getAllImages(email);

        View view = inflater.inflate(R.layout.fragment_image, container, false);
        recyclerView = view.findViewById(R.id.recycleViewImageGallery);
        buttonDecryptAll = view.findViewById(R.id.decryptAll);
        progressBar = view.findViewById(R.id.progressBar);

        adapter = new ImageRecycleViewAdapter(imagesToView, getContext(), new IClickImageListener() {
            @Override
            public void onClickImageListener(TempImageToView encryptedImage) {
                onClickGoToImageView(encryptedImage);
            }

            @Override
            public void onLongClickImageListener(List<TempImageToView> TempImageToView) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to decrypt these files? (" + TempImageToView.size() + " files)").setTitle("Confirmation").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (TempImageToView f : TempImageToView) {
                            AddImageToDecryptTask task = new AddImageToDecryptTask(getContext());
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

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

        for (EncryptFile f : encryptedImages) {
            new AddImageToViewTask().execute(f);
        }

        buttonDecryptAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TempImageToView t : imagesToView) {
                    AddImageToDecryptTask task = new AddImageToDecryptTask(getContext());
                    task.execute(t);
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setImagesToView(imagesToView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        encryptedImages.clear();
        imagesToView.clear();
    }

    private void onClickGoToImageView(TempImageToView encryptedImage) {
        Intent intent = new Intent(getContext(), ImageViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_image", encryptedImage.getFile());
        intent.putExtras(bundle);
        this.startActivity(intent);
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
