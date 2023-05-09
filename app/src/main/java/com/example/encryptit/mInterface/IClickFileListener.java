package com.example.encryptit.mInterface;

import com.example.encryptit.model.EncryptFile;

import java.util.List;

public interface IClickFileListener {
    void onLongClickImageListener(List<EncryptFile> files);
}
