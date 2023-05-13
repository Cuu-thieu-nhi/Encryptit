package com.example.encryptit.utils;

public class GetFileName {
    public static String getFileNameAndExtension (String path) {
        StringBuilder reversePath = new StringBuilder(path);
        String reverseFileName =  reversePath.reverse().toString();
        StringBuilder fileName = new StringBuilder();
        for (int i = 0; i < reverseFileName.length(); i ++) {
            if (reverseFileName.charAt(i) != '/')
                fileName.append(reverseFileName.charAt(i));
            else break;
        }
        return fileName.reverse().toString();
    }

    public static String getFileName (String path) {
        StringBuilder fileNameAndExtension = new StringBuilder(getFileNameAndExtension(path));
        StringBuilder fileName = new StringBuilder();
        for (int i = 0; i < fileNameAndExtension.length(); i ++) {
            if (fileNameAndExtension.charAt(i) != '.')
                fileName.append(fileNameAndExtension.charAt(i));
            else break;
        }
        return fileName.toString();
    }

    public static String getFileExtension (String path) {
        StringBuilder fileNameAndExtension = new StringBuilder(getFileNameAndExtension(path));
        StringBuilder fileExtension = new StringBuilder();
        for (int i = fileNameAndExtension.length() - 1; i >= 0; i --) {
            if (fileNameAndExtension.charAt(i) != '.')
                fileExtension.append(fileNameAndExtension.charAt(i));
            else break;
        }
        return fileExtension.reverse().toString();
    }

    public static String getFileLocation (String path) {
        StringBuilder pathh = new StringBuilder(path);
        int last = 0;
        StringBuilder fileLocation = new StringBuilder();
        for (int i = 0; i < pathh.length(); i ++) {
            if (path.charAt(i) == '/')
                last = i;
        }
        for (int i = 0; i < last; i ++) {
            fileLocation.append(pathh.charAt(i));
        }
        return fileLocation.toString();
    }


}
