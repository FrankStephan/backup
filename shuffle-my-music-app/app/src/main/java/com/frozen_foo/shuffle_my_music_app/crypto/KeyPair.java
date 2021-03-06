package com.frozen_foo.shuffle_my_music_app.crypto;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

/**
 * Created by Frank on 10.07.2017.
 */

public class KeyPair {

	private static final String ALIAS = "com.frozen_foo.shuffle_my_music_app.security.alias";

	private KeyStore androidKeyStore;

	public KeyPair() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
			NoSuchProviderException, InvalidAlgorithmParameterException {
		androidKeyStore = loadKeyStore();
		createKeyPairIfNecessary(androidKeyStore);
	}

	private KeyStore loadKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
			IOException {
		KeyStore androidKeyStore = KeyStore.getInstance("AndroidKeyStore");
		androidKeyStore.load(null);
		return androidKeyStore;
	}

	private void createKeyPairIfNecessary(KeyStore androidKeyStore) throws KeyStoreException, NoSuchProviderException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		if (!androidKeyStore.containsAlias(ALIAS)) {
			KeyPairGenerator keyPairGenerator =
					KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
			keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(ALIAS, KeyProperties.PURPOSE_DECRYPT)
					.setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
					.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP).build());
			keyPairGenerator.generateKeyPair();
		}
	}

	public PublicKey publicKey() throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) androidKeyStore.getEntry(ALIAS, null);
		return privateKeyEntry.getCertificate().getPublicKey();
	}

	public PrivateKey privateKey() throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) androidKeyStore.getEntry(ALIAS, null);
		return privateKeyEntry.getPrivateKey();
	}
}
