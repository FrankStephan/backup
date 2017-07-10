package com.frozen_foo.shuffle_my_music_app.security;

import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

import javax.security.auth.x500.X500Principal;

/**
 * Created by Frank on 10.07.2017.
 */

public class KeyStoreService {

	private static final String ALIAS = "com.frozen_foo.shuffle_my_music_app.security.alias";

	private KeyStore androidKeyStore;

	public KeyStoreService() throws CertificateException, NoSuchAlgorithmException,
			KeyStoreException, IOException, NoSuchProviderException,
			InvalidAlgorithmParameterException {
		androidKeyStore = loadKeyStore();
		createKeyPairIfNecessary(androidKeyStore);
	}

	private KeyStore loadKeyStore() throws KeyStoreException, CertificateException,
			NoSuchAlgorithmException, IOException {
		KeyStore androidKeyStore = KeyStore.getInstance("AndroidKeyStore");
		androidKeyStore.load(null);
		return androidKeyStore;
	}

	private void createKeyPairIfNecessary(KeyStore androidKeyStore) throws KeyStoreException,
			NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {


		ndroidKeyStore.deleteEntry(ALIAS);

		if (!androidKeyStore.containsAlias(ALIAS)) {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
					KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
			keyPairGenerator.initialize(
					new KeyGenParameterSpec.Builder(
							ALIAS,
							KeyProperties.PURPOSE_DECRYPT)
							.setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
							.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
							.build());
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
		}
	}

	public KeyStore keyStore() {
		return androidKeyStore;
	}

	public String alias() {
		return ALIAS;
	}

	public PublicKey publicKey() throws UnrecoverableEntryException, NoSuchAlgorithmException,
			KeyStoreException {
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)androidKeyStore.getEntry(ALIAS, null);
       return privateKeyEntry.getCertificate().getPublicKey();
	}

	public PrivateKey privateKey() throws UnrecoverableEntryException, NoSuchAlgorithmException,
			KeyStoreException {
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)androidKeyStore.getEntry(ALIAS, null);
		return privateKeyEntry.getPrivateKey();
	}
}
