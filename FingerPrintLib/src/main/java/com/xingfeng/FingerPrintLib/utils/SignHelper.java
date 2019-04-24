package com.xingfeng.FingerPrintLib.utils;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

import static android.content.ContentValues.TAG;

@RequiresApi(Build.VERSION_CODES.P)
public class SignHelper {
    private Signature signData(String alias, String inputStr) {
        try {
            byte[] data = inputStr.getBytes();
            KeyStore ks = KeyStore.getInstance(SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(alias, null);

            if (entry == null) {
                Log.w(TAG, "No key found under alias: " + alias);
                Log.w(TAG, "Exiting signData()...");
                return null;
            }

            if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                Log.w(TAG, "Not an instance of a PrivateKeyEntry");
                Log.w(TAG, "Exiting signData()...");
                return null;
            }
            Signature s = Signature.getInstance(SecurityConstants.SIGNATURE_SHA256withRSA);
            s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
            s.update(data);
            byte[] signature = s.sign();
            String result = Base64.encodeToString(signature, Base64.DEFAULT);

            Log.i("DEBUG_TEST", "sign result:" + result);
            return s;
        } catch (Exception e) {

        }

        return null;
    }

    private boolean verfiySign(String alias, String input, String signatureStr) {
        try {
            byte[] data = input.getBytes();
            byte[] signature;

            if (signatureStr == null) {
                Log.w(TAG, "Invalid signature.");
                Log.w(TAG, "Exiting verifyData()...");
                return false;
            }

            signature = Base64.decode(signatureStr, Base64.DEFAULT);

            KeyStore ks = KeyStore.getInstance(SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(alias, null);

            if (entry == null) {
                Log.w(TAG, "No key found under alias: " + alias);
                Log.w(TAG, "Exiting signData()...");
                return false;
            }

            if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                Log.w(TAG, "Not an instance of a PrivateKeyEntry");
                Log.w(TAG, "Exiting signData()...");
                return false;
            }
            Signature s = Signature.getInstance(SecurityConstants.SIGNATURE_SHA256withRSA);
            s.initVerify(((KeyStore.PrivateKeyEntry) entry).getCertificate());
            s.update(data);

            return s.verify(signature);
        } catch (Exception e) {

        }

        return false;
    }

    public KeyPair createKeys(String alias, Context context) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // BEGIN_INCLUDE(create_valid_dates)
        // Create a start and end time, for the validity range of the key pair that's about to be
        // generated.
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 1);
        //END_INCLUDE(create_valid_dates)


        // BEGIN_INCLUDE(create_spec)
        // The KeyPairGeneratorSpec object is how parameters for your key pair are passed
        // to the KeyPairGenerator.  For a fun home game, count how many classes in this sample
        // start with the phrase "KeyPair".
        KeyPairGeneratorSpec spec =
                new KeyPairGeneratorSpec.Builder(context)
                        // You'll use the alias later to retrieve the key.  It's a key for the key!
                        .setAlias(alias)
                        // The subject used for the self-signed certificate of the generated pair
                        .setSubject(new X500Principal("CN=" + alias))
                        // The serial number used for the self-signed certificate of the
                        // generated pair.
                        .setSerialNumber(BigInteger.valueOf(1337))
                        // Date range of validity for the generated pair.
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
        // END_INCLUDE(create_spec)

        // BEGIN_INCLUDE(create_keypair)
        // Initialize a KeyPair generator using the the intended algorithm (in this example, RSA
        // and the KeyStore.  This example uses the AndroidKeyStore.
        KeyPairGenerator kpGenerator = KeyPairGenerator
                .getInstance(SecurityConstants.TYPE_RSA,
                        SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        kpGenerator.initialize(spec);
        KeyPair kp = kpGenerator.generateKeyPair();
        Log.d(TAG, "Public Key is: " + kp.getPublic().toString());
        // END_INCLUDE(create_keypair)
        return kp;
    }
}
