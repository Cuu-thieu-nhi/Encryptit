package com.example.encryptit.utils;

public class GetFileName {
    public static String getFileNameAndExtension (String path) {
        StringBuffer reversePath = new StringBuffer(path);
        String reverseFileName =  reversePath.reverse().toString();
        StringBuffer fileName = new StringBuffer();
        for (int i = 0; i < reverseFileName.length(); i ++) {
            if (reverseFileName.charAt(i) != '/')
                fileName.append(reverseFileName.charAt(i));
            else break;
        }
        return fileName.reverse().toString();
    }

    public static String getFileName (String path) {
        StringBuffer fileNameAndExtension = new StringBuffer(getFileNameAndExtension(path));
        StringBuffer fileName = new StringBuffer();
        for (int i = 0; i < fileNameAndExtension.length(); i ++) {
            if (fileNameAndExtension.charAt(i) != '.')
                fileName.append(fileNameAndExtension.charAt(i));
            else break;
        }
        return fileName.toString();
    }

    public static String getFileExtension (String path) {
        StringBuffer fileNameAndExtension = new StringBuffer(getFileNameAndExtension(path));
        StringBuffer fileExtension = new StringBuffer();
        for (int i = fileNameAndExtension.length() - 1; i >= 0; i --) {
            if (fileNameAndExtension.charAt(i) != '.')
                fileExtension.append(fileNameAndExtension.charAt(i));
            else break;
        }
        return fileExtension.reverse().toString();
    }

    public static String getFileLocation (String path) {
        StringBuffer pathh = new StringBuffer(path);
        int last = 0;
        StringBuffer fileLocation = new StringBuffer();
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
