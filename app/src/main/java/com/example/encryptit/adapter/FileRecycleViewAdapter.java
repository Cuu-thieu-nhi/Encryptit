package com.example.encryptit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptit.R;
import com.example.encryptit.mInterface.IClickFileListener;
import com.example.encryptit.model.EncryptFile;

import java.util.ArrayList;
import java.util.List;

public class FileRecycleViewAdapter extends RecyclerView.Adapter<FileRecycleViewAdapter.ViewHolder> {
    private final IClickFileListener iClickFileListener;
    private final List<EncryptFile> selectedFiles = new ArrayList<>();
    Context context;
    private List<EncryptFile> files;

    public FileRecycleViewAdapter(List<EncryptFile> files, Context context, IClickFileListener iClickFileListener) {
        this.files = files;
        this.context = context;
        this.iClickFileListener = iClickFileListener;
    }

    public void setFiles(List<EncryptFile> files) {
        this.files = files;
        selectedFiles.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FileRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_single_file, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EncryptFile f = files.get(position);
        boolean isSelected = selectedFiles.contains(f);
        holder.setSelected(isSelected);

        if (isSelected) {
            int color = ContextCompat.getColor(context, R.color.file_selected);
            holder.linearLayout.setBackgroundColor(color);
            Log.d("FileRecycleViewAdapter", "onClick: un-select to select " + position + " " + holder.getAdapterPosition() + " " + holder.linearLayout.getBackground());

        } else {
            int color = ContextCompat.getColor(context, R.color.white);
            holder.linearLayout.setBackgroundColor(color);
            Log.d("FileRecycleViewAdapter", "onClick: select to un-select " + position + " " + holder.getAdapterPosition() + " " + holder.linearLayout.getBackground());
        }



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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isSelected() == null) holder.setSelected(false);

                boolean isSelected = !holder.isSelected();
                holder.setSelected(isSelected);
                if (isSelected) {
                    int color = ContextCompat.getColor(context, R.color.file_selected);
                    selectedFiles.add(files.get(position));
                    holder.linearLayout.setBackgroundColor(color);
                    Log.d("FileRecycleViewAdapter", "onClick: un-select to select " + position + " " + holder.getAdapterPosition() + " " + holder.linearLayout.getBackground());

                } else {
                    int color = ContextCompat.getColor(context, R.color.white);
                    selectedFiles.remove(files.get(position));
                    holder.linearLayout.setBackgroundColor(color);
                    Log.d("FileRecycleViewAdapter", "onClick: select to un-select " + position + " " + holder.getAdapterPosition() + " " + holder.linearLayout.getBackground());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.isSelected() && !selectedFiles.isEmpty()) {
                    iClickFileListener.onLongClickImageListener(selectedFiles);
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        if (files != null) {
            return files.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView imageView;
        TextView textView;
        Boolean selected;

        public ViewHolder(@NonNull View itemView) {
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
}
