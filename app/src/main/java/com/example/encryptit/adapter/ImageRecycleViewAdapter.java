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
import com.example.encryptit.mInterface.IClickImageListener;
import com.example.encryptit.model.TempImageToView;

import java.util.List;

public class ImageRecycleViewAdapter extends RecyclerView.Adapter<ImageRecycleViewAdapter.ViewHolder> {
    Context context;
    private final IClickImageListener iClickImageListener;
    private List<TempImageToView> decryptedImages;

    public ImageRecycleViewAdapter(List<TempImageToView> decryptedImages, Context context, IClickImageListener iClickImageListener) {
        this.decryptedImages = decryptedImages;
        this.iClickImageListener = iClickImageListener;
        this.context = context;
    }

    public void setDecryptedImages(List<TempImageToView> decryptedImages) {
        this.decryptedImages = decryptedImages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_single_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRecycleViewAdapter.ViewHolder holder, int position) {
        final TempImageToView encryptedImage = decryptedImages.get(position);
        if (encryptedImage == null) return;
        Glide.with(context).load(encryptedImage.getData()).into(holder.imageView);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickImageListener.onClickImageListener(encryptedImage);
            }
        });
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
