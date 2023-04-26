package com.example.encryptit.utils;

import android.util.Log;

import java.io.File;

public class DeleteFile {
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                Log.i("FileUtils", "File deleted: " + filePath);
            } else {
                Log.e("FileUtils", "Failed to delete file: " + filePath);
            }
        } else {
            Log.e("FileUtils", "File not found: " + filePath);
        }
    }

}
