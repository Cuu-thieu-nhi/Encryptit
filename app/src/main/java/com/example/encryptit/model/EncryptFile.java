package com.example.encryptit.model;

import java.io.Serializable;

public class EncryptFile implements Serializable {
    private String filePath;  // /storage/emulated/0/Download/abc.txt
    private String fileNameAndExtension; // abc.txt
    private String fileName; // abc
    private String fileExtension; // .txt
    private String fileLocation; // /storage/emulated/0/Download
    private String alias;
    private Boolean isImage;

    public EncryptFile() {
    }

    public EncryptFile(String filePath, String fileNameAndExtension, String fileName, String fileExtension, String fileLocation, String alias, Boolean isImage) {
        this.filePath = filePath;
        this.fileNameAndExtension = fileNameAndExtension;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileLocation = fileLocation;
        this.alias = alias;
        this.isImage = isImage;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileNameAndExtension() {
        return fileNameAndExtension;
    }

    public void setFileNameAndExtension(String fileNameAndExtension) {
        this.fileNameAndExtension = fileNameAndExtension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Boolean getImage() {
        return isImage;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "EncryptFile{" + "filePath='" + filePath + '\'' + ", fileNameAndExtension='" + fileNameAndExtension + '\'' + ", fileName='" + fileName + '\'' + ", fileExtension='" + fileExtension + '\'' + ", fileLocation='" + fileLocation + '\'' + ", alias='" + alias + '\'' + ", isImage=" + isImage + '}';
    }
}
