package com.administrator.learndemo.keystore;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Created by erfli on 2/24/17.
 */

class Encryptor {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private KeyStore mKeyStore;
    private byte[] encryption;
    private byte[] iv;

    Encryptor() {
        try {
            mKeyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            mKeyStore.load(null);
        } catch (Exception e) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    byte[] encryptText(final String alias, final String textToEncrypt)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

        iv = cipher.getIV();

        return (encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    private Key getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException, KeyStoreException, UnrecoverableKeyException{
        Key secretKey = null;
        if (mKeyStore.isKeyEntry(KeyStoreActivity.TAG)) {
            Log.i("KeyStoreActivity", "证书存在");
            //mKeyStore.deleteEntry(KeyStoreActivity.TAG);
            Enumeration<String> aliase =  mKeyStore.aliases();
            while (aliase.hasMoreElements()) {
                Log.i("KeyStoreActivity", "证书存在 secretKey:" + aliase.nextElement());
            }
            secretKey =  mKeyStore.getKey(KeyStoreActivity.TAG, null);
            Log.i("KeyStoreActivity", "证书存在 secretKey:" + secretKey);
        } else {
            Log.i("KeyStoreActivity", "证书不存在");
            secretKey = generateKey();
            Enumeration<String> aliase =  mKeyStore.aliases();
            while (aliase.hasMoreElements()) {
                Log.i("KeyStoreActivity", "证书存在 secretKey:" + aliase.nextElement());
            }
        }
        return secretKey;
    }

    byte[] getEncryption() {
        return encryption;
    }

    byte[] getIv() {
        return iv;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private SecretKey generateKey() throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {
        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        keyGenerator.init(new KeyGenParameterSpec.Builder(KeyStoreActivity.TAG,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build());

        return keyGenerator.generateKey();
    }
}
