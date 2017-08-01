package com.frozen_foo.shuffle_my_music_app.crypto;

import android.content.Context;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Frank on 07.07.2017.
 */

public class Cryptifier {

    private static final String CREDENTIALS_FILE = "credentials";

    KeyPair keyPair;

    public Cryptifier() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException {
        this.keyPair = new KeyPair();
    }

    public String encrypt(String original) throws UnrecoverableEntryException,
            NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException, IOException {
        Cipher input = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        input.init(Cipher.ENCRYPT_MODE, keyPair.publicKey());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, input)) {
            cipherOutputStream.write(original.getBytes("UTF-8"));
        }
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public String decrypt(String encodedString) throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IOException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		cipher.init(Cipher.DECRYPT_MODE, keyPair.privateKey());

        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(Base64.decode(encodedString, Base64.DEFAULT)), cipher);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte)nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }

       return new String(bytes, 0, bytes.length, "UTF-8");
    }

    public void storeSmbCredentials(String login, String password, Context context) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableEntryException, NoSuchProviderException, InvalidAlgorithmParameterException, CertificateException {
        File file = new File(context.getFilesDir(), CREDENTIALS_FILE);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        String encodedLogin = encrypt(login);
        String encodedPassword = encrypt(password);
        fos.write(encodedLogin.getBytes("UTF-8"));
        fos.write(System.getProperty("line.separator").getBytes());
        fos.write(encodedPassword.getBytes());
    }
}
