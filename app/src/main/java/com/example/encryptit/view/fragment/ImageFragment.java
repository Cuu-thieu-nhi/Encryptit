package com.example.encryptit.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
import com.example.encryptit.view.ImageViewActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment {

    public static List<EncryptFile> encryptedImages = new ArrayList<>();
    public static List<TempImageToView> imagesToView = new ArrayList<>();
    public static ImageRecycleViewAdapter adapter;
    FileDAO imageDb;
    RecyclerView recyclerView;
    ImageButton buttonDecryptAll;

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
        imageDb = new FileDAO(getContext());
        encryptedImages = imageDb.getAllImages();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        recyclerView = view.findViewById(R.id.recycleViewImageGallery);
        buttonDecryptAll = view.findViewById(R.id.decryptAll);

        adapter = new ImageRecycleViewAdapter(imagesToView, getContext(), new IClickImageListener() {
            @Override
            public void onClickImageListener(TempImageToView encryptedImage) {
                onClickGoToImageView(encryptedImage);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
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

    private void onClickGoToImageView(TempImageToView encryptedImage) {
        Intent intent = new Intent(getContext(), ImageViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_image", encryptedImage.getFile());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
}
