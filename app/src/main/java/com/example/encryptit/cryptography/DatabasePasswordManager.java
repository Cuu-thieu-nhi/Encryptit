package com.example.encryptit.cryptography;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DatabasePasswordManager {
    private static final String SECRET_KEY = "6A8A6C17610A4D820E7171DE95785D28";
    private static final String ALGORITHM = "AES";

    public static String encrypt(String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedPassword = cipher.doFinal(password.getBytes());
        return Base64.encodeToString(encryptedPassword, Base64.DEFAULT);
    }

    public static String decrypt(String encryptedPassword) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedPassword = cipher.doFinal(Base64.decode(encryptedPassword, Base64.DEFAULT));
        return new String(decryptedPassword);
    }
}
