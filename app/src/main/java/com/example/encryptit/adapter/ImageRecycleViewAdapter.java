package com.example.encryptit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.encryptit.R;
import com.example.encryptit.mInterface.IClickImageListener;
import com.example.encryptit.model.TempImageToView;

import java.util.ArrayList;
import java.util.List;

public class ImageRecycleViewAdapter extends RecyclerView.Adapter<ImageRecycleViewAdapter.ViewHolder> {
    private final IClickImageListener iClickImageListener;
    private final List<TempImageToView> selectedFiles = new ArrayList<>();
    Context context;
    private List<TempImageToView> decryptedImages;

    public ImageRecycleViewAdapter(List<TempImageToView> decryptedImages, Context context, IClickImageListener iClickImageListener) {
        this.decryptedImages = decryptedImages;
        this.context = context;
        this.iClickImageListener = iClickImageListener;
    }

    public void setDecryptedImages(List<TempImageToView> decryptedImages) {
        this.decryptedImages = decryptedImages;
        selectedFiles.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_single_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRecycleViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final TempImageToView encryptedImage = decryptedImages.get(position);
        if (encryptedImage == null) return;

        boolean isSelected = selectedFiles.contains(encryptedImage);
        holder.setSelected(isSelected);

        Glide.with(context).load(encryptedImage.getData()).into(holder.imageView);

        if (isSelected) {
            int color = ContextCompat.getColor(context, R.color.file_selected);
            holder.relativeLayout.setBackgroundColor(color);
            Log.d("FileRecycleViewAdapter", "onClick: un-select to select " + position + " " + holder.getAdapterPosition());

        } else {
            int color = ContextCompat.getColor(context, R.color.main);
            holder.relativeLayout.setBackgroundColor(color);
            Log.d("FileRecycleViewAdapter", "onClick: select to un-select " + position + " " + holder.getAdapterPosition());
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.isSelected() && !selectedFiles.isEmpty()) {
                    iClickImageListener.onLongClickImageListener(selectedFiles);
                } else {
                    iClickImageListener.onClickImageListener(encryptedImage);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (holder.isSelected() == null) holder.setSelected(false);

                boolean isSelected = !holder.isSelected();
                holder.setSelected(isSelected);
                if (isSelected) {
                    int color = ContextCompat.getColor(context, R.color.file_selected);
                    selectedFiles.add(decryptedImages.get(position));
                    holder.relativeLayout.setBackgroundColor(color);
                    Log.d("ImageRecycleViewAdapter", "onClick: un-select to select " + position + " " + holder.getAdapterPosition());

                } else {
                    int color = ContextCompat.getColor(context, R.color.main);
                    selectedFiles.remove(decryptedImages.get(position));
                    holder.relativeLayout.setBackgroundColor(color);
                    Log.d("ImageRecycleViewAdapter", "onClick: select to un-select " + position + " " + holder.getAdapterPosition());
                }
                return true;
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

        Boolean selected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selected = false;
            imageView = itemView.findViewById(R.id.image);
            relativeLayout = itemView.findViewById(R.id.relative);
        }

        public Boolean isSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }
    }
}
