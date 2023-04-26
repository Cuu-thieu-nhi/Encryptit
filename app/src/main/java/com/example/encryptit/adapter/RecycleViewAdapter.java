package com.example.encryptit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.encryptit.R;
import com.example.encryptit.model.TempFileToView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    Context context;
    private List<TempFileToView> decryptedImages;


    public RecycleViewAdapter(List<TempFileToView> decryptedImages, Context context) {
        this.decryptedImages = decryptedImages;
        this.context = context;
    }

    public void setDecryptedImages(List<TempFileToView> decryptedImages) {
        this.decryptedImages = decryptedImages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_single_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        final TempFileToView encryptedImage = decryptedImages.get(position);
        if (encryptedImage == null)
            return;
        Glide.with(context).load(encryptedImage.getData()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (decryptedImages != null) {
            return decryptedImages.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ConstraintLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            relativeLayout = itemView.findViewById(R.id.relative);
        }
    }

}
