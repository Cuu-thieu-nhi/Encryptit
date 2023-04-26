package com.example.encryptit.model;

public class TempFileToView {
    EncryptFile file;
    byte[] data;

    public TempFileToView(EncryptFile file, byte[] data) {
        this.file = file;
        this.data = data;
    }

    public TempFileToView() {
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