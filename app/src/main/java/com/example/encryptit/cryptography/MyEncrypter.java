package com.example.encryptit.cryptography;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class MyEncrypter {
    private static final String TAG = "MyEncrypter";

    public static void encryptFile(String inputFilePath, String outputFilePath, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] ivBytes = cipher.getIV();


            FileInputStream inputStream = new FileInputStream(inputFilePath);
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            outputStream.write(ivBytes);

            BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[8192]; // đọc 8KB mỗi lần
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                byte[] outputBuffer = cipher.update(buffer, 0, bytesRead);
                outputStream.write(outputBuffer);
            }

            byte[] outputBuffer = cipher.doFinal();
            outputStream.write(outputBuffer);

            bis.close();
            inputStream.close();
            outputStream.close();

            File f = new File(inputFilePath);
            f.delete();
        } catch (NoSuchPaddingException e) {
            Log.d(TAG, "encryptFile: NoSuchPaddingException");
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            Log.d(TAG, "encryptFile: IllegalBlockSizeException");
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void decryptFile(String inputFilePath, String outputFilePath, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            FileInputStream inputStream = new FileInputStream(inputFilePath);

            byte[] ivBytes = new byte[16];
            inputStream.read(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            FileOutputStream outputStream = new FileOutputStream(outputFilePath);

            BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[8192]; // đọc 8KB mỗi lần
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                byte[] outputBuffer = cipher.update(buffer, 0, bytesRead);
                outputStream.write(outputBuffer);
            }

            byte[] outputBuffer = cipher.doFinal();
            outputStream.write(outputBuffer);

            bis.close();
            inputStream.close();
            outputStream.close();

            File f = new File(inputFilePath);
            f.delete();
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptFileToViewTemporary(String inputFilePath, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            FileInputStream inputStream = new FileInputStream(inputFilePath);

            byte[] ivBytes = new byte[16];
            inputStream.read(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[8192]; // đọc 8KB mỗi lần
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                byte[] outputBuffer = cipher.update(buffer, 0, bytesRead);
                outputStream.write(outputBuffer);
            }

            byte[] outputBuffer = cipher.doFinal();
            outputStream.write(outputBuffer);

            bis.close();
            inputStream.close();
            outputStream.close();
            return outputStream.toByteArray();

        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
