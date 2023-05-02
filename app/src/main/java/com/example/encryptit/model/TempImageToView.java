package com.example.encryptit.model;

import java.io.Serializable;

public class TempImageToView implements Serializable {
    EncryptFile file;
    byte[] data;

    public TempImageToView(EncryptFile file, byte[] data) {
        this.file = file;
        this.data = data;
    }

    public TempImageToView() {
    }

    public EncryptFile getFile() {
        return file;
    }

    public void setFile(EncryptFile file) {
        this.file = file;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
