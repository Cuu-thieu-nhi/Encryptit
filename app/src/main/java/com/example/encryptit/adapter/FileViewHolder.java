package com.example.encryptit.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;

public class FileViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    ImageView imageView;
    TextView textView;
    Boolean selected;

    public FileViewHolder(@NonNull View itemView) {
        super(itemView);
        selected = false;
        linearLayout = itemView.findViewById(R.id.file_linear_layout);
        imageView = itemView.findViewById(R.id.imageView);
        textView = itemView.findViewById(R.id.textVieww);
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
