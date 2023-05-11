package com.example.encryptit.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    ConstraintLayout relativeLayout;

    Boolean selected;

    public ImageViewHolder(@NonNull View itemView) {
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