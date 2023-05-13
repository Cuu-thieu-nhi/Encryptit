package com.example.encryptit.cryptography;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class MyKeyStore {

    private static final String KEY_ALIAS_PREFIX = "my-key-alias";

    public static SecretKey generateSecretKey(String alias) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(getFullAlias(alias), KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());

            return keyGenerator.generateKey();
        } catch (InvalidAlgorithmParameterException e) {
            Log.d("MyKeyStore", "generateSecretKey: error");
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFullAlias(String alias) {
        return KEY_ALIAS_PREFIX + "-" + alias;
    }

    public static void saveSecretKey(SecretKey secretKey, String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(getFullAlias(alias))) {
                KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
                keyStore.setEntry(getFullAlias(alias), secretKeyEntry, null);
                Log.d("MyKeyStore", "saveSecretKey: " + secretKey.toString());
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 IOException e) {
            Log.d("MyKeyStore", "saveSecretKey: error " + alias);
            e.printStackTrace();
        }
    }

    public static SecretKey loadSecretKey(String alias) {
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
