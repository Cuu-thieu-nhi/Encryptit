package com.example.encryptit.utils;

import com.example.encryptit.model.EncryptFile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MySort {
    public static void sortByNameAndExtension(List<EncryptFile> encryptFiles) {
        Collections.sort(encryptFiles, new Comparator<EncryptFile>() {
            @Override
            public int compare(EncryptFile o1, EncryptFile o2) {
                int nameComparison = o1.getFileName().compareToIgnoreCase(o2.getFileName());
                if (nameComparison != 0) {
                    return nameComparison;
                } else {
                    return o1.getFileExtension().compareToIgnoreCase(o2.getFileExtension());
                }
            }
        });
    }

    public static void sortByExtensionAndName(List<EncryptFile> encryptFiles) {
        Collections.sort(encryptFiles, new Comparator<EncryptFile>() {
            @Override
            public int compare(EncryptFile o1, EncryptFile o2) {
                int extensionComparison = o1.getFileExtension().compareToIgnoreCase(o2.getFileExtension());
                if (extensionComparison != 0) {
                    return extensionComparison;
                } else {
                    return o1.getFileName().compareToIgnoreCase(o2.getFileName());
                }
            }
        });
    }
}
