package com.example.encryptit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;
import com.example.encryptit.model.EncryptFile;

import java.util.List;

public class FileRecycleViewAdapter extends RecyclerView.Adapter<FileRecycleViewAdapter.ViewHolder>{
    Context context;
    private List<EncryptFile> files;

    public FileRecycleViewAdapter (List<EncryptFile> files, Context context) {
        this.files = files;
        this.context = context;
    }

    public void setFiles(List<EncryptFile> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FileRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_single_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileRecycleViewAdapter.ViewHolder holder, int position) {
        final EncryptFile f = files.get(position);
        if (f == null)
            return;

        holder.imageView.setImageResource(R.drawable.baseline_attach_file_24);
        holder.textView.setText(f.getFileNameAndExtension());
    }

    @Override
    public int getItemCount() {
        if (files != null) {
            return files.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textVieww);
        }
    }
}
