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

public class FileRecycleViewAdapter extends RecyclerView.Adapter<FileRecycleViewAdapter.ViewHolder> {
    Context context;
    private List<EncryptFile> files;

    public FileRecycleViewAdapter(List<EncryptFile> files, Context context) {
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
        if (f == null) return;
        String ex = f.getFileExtension();
        switch (ex) {
            case "doc":
            case "docx":
                holder.imageView.setImageResource(R.drawable.doc);
                break;
            case "flv":
                holder.imageView.setImageResource(R.drawable.flv);
                break;
            case "gif":
                holder.imageView.setImageResource(R.drawable.gif);
                break;
            case "jpg":
                holder.imageView.setImageResource(R.drawable.jpg);
                break;
            case "mp3":
            case "mp4":
                holder.imageView.setImageResource(R.drawable.mp3);
                break;
            case "pdf":
                holder.imageView.setImageResource(R.drawable.pdf);
                break;
            case "png":
                holder.imageView.setImageResource(R.drawable.png);
                break;
            case "ppt":
                holder.imageView.setImageResource(R.drawable.ppt);
                break;
            case "raw":
                holder.imageView.setImageResource(R.drawable.raw);
                break;
            case "txt":
                holder.imageView.setImageResource(R.drawable.txt);
                break;
            case "xlsx":
            case "xlxs":
                holder.imageView.setImageResource(R.drawable.xls);
                break;
            case "xml":
                holder.imageView.setImageResource(R.drawable.xml);
                break;
            case "rar":
            case "zip":
                holder.imageView.setImageResource(R.drawable.zip);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.raw);
                break;
        }
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
