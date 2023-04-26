package com.example.encryptit.cryptography;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;


public class MyKeyStore {

    private static final String KEY_ALIAS_PREFIX = "my-key-alias";
    public static SecretKey generateSecretKey (Context context, String alias) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(getFullAlias(alias), KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey;
        } catch (InvalidAlgorithmParameterException e) {
            Log.d("MyKeyStore", "generateSecretKey: error");
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

//    public static SecretKey generateSecretKey(Context context, String alias) {
//        try {
//            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
//            keyStore.load(null);
//            if (!keyStore.containsAlias(alias)) {
//                KeyGenerator keyGenerator = KeyGenerator.getInstance(
//                        KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//                KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
//                        alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                        .setUserAuthenticationRequired(true)
//                        .setUserAuthenticationValidityDurationSeconds(30);
//
//                keyGenerator.init(builder.build());
//                keyGenerator.generateKey();
//            }
//            Key secretKey = keyStore.getKey(alias, null);
//            return (SecretKey) secretKey;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    private static String getFullAlias(String alias) {
        return KEY_ALIAS_PREFIX + "-" + alias;
    }

    public static void saveSecretKey(Context context, SecretKey secretKey, String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
            keyStore.setEntry(getFullAlias(alias), secretKeyEntry, null);
            Log.d("MyKeyStore", "saveSecretKey: " + secretKey.toString());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            Log.d("MyKeyStore", "saveSecretKey: error " + alias);
            e.printStackTrace();
        }
    }

    public static SecretKey loadSecretKey(Context context, String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            SecretKey secretKey = (SecretKey) keyStore.getKey(getFullAlias(alias), null);
            Log.d("MyKeyStore", "loadSecretKey: " + secretKey.toString());
            return secretKey;
        } catch (Exception e) {
            Log.d("MyKeyStore", "loadSecretKey: error " + alias);
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSecretKeySaved(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("key_saved", false);
    }

}
