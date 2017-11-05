package com.frozen_foo.shuffle_my_music_app.settings;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_app.crypto.Cryptifier;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by Frank on 12.07.2017.
 */

public class SettingsAccess {

	private static String DEFAULT_LOCAL_DIR = "_shuffle-my-music";


	private SettingsCache cache;

	public SettingsAccess() {
		cache = new SettingsCache();
	}

	public void preloadSettings(Context context) throws SettingsAccessException {
		readSettings(context);
	}

	public void writeSettings(String ip, String username, String password, String localDir, String remoteDir, Context context) throws
			MissingSettingsException, SettingsAccessException {
		if (StringUtils.isNoneEmpty(ip, username, remoteDir)) {
			String   newPassword = StringUtils.defaultIfEmpty(password, readSettings(context).getPassword());
			String   newLocalDir = StringUtils.defaultIfEmpty(localDir, DEFAULT_LOCAL_DIR);
			Settings encryptedSettings    = encrypt(ip, username, newPassword, newLocalDir, remoteDir);
			new SettingsIO().writeSettings(encryptedSettings, context);
			cache.setSettings(encryptedSettings);
			cache.setValid(true);
		} else {
			throw new MissingSettingsException();
		}
	}

	private Settings encrypt(String ip, String username, String password, String localDir, String remoteDir) throws
			SettingsAccessException, MissingSettingsException {
		try {
			Cryptifier cryptifier = new Cryptifier();
			return new Settings(cryptifier.encrypt(ip), cryptifier.encrypt(username), cryptifier.encrypt(password),
					cryptifier.encrypt(localDir), cryptifier.encrypt(remoteDir));
		} catch (CertificateException e) {
			throw new SettingsAccessException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new SettingsAccessException(e);
		} catch (KeyStoreException e) {
			throw new SettingsAccessException(e);
		} catch (NoSuchProviderException e) {
			throw new SettingsAccessException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new SettingsAccessException(e);
		} catch (IOException e) {
			throw new SettingsAccessException(e);
		} catch (NoSuchPaddingException e) {
			throw new SettingsAccessException(e);
		} catch (UnrecoverableEntryException e) {
			throw new SettingsAccessException(e);
		} catch (InvalidKeyException e) {
			throw new SettingsAccessException(e);
		}
	}

	public Settings readSettings(Context context) throws SettingsAccessException {
		Settings encryptedSettings;
		if (cache.isValid()) {
			encryptedSettings = cache.getSettings();
		} else {
			encryptedSettings = new SettingsIO().readSettings(context);
			cache.setSettings(encryptedSettings);
			cache.setValid(true);
		}
		return decrypt(encryptedSettings.getIp(), encryptedSettings.getUsername(), encryptedSettings.getPassword(),
				encryptedSettings.getLocalDir(), encryptedSettings.getRemoteDir());
	}

	private Settings decrypt(String ip, String username, String password, String localDir, String remoteDir) throws
			SettingsAccessException {
		return new Settings(decryptString(ip), decryptString(username), decryptString(password),
				decryptString(localDir), decryptString(remoteDir));
	}

	private String decryptString(String encryptedString) throws SettingsAccessException {
		try {
			Cryptifier cryptifier = new Cryptifier();
			return StringUtils.isNotEmpty(encryptedString) ? cryptifier.decrypt(encryptedString) : "";
		} catch (CertificateException e) {
			throw new SettingsAccessException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new SettingsAccessException(e);
		} catch (KeyStoreException e) {
			throw new SettingsAccessException(e);
		} catch (NoSuchProviderException e) {
			throw new SettingsAccessException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new SettingsAccessException(e);
		} catch (IOException e) {
			throw new SettingsAccessException(e);
		} catch (NoSuchPaddingException e) {
			throw new SettingsAccessException(e);
		} catch (UnrecoverableEntryException e) {
			throw new SettingsAccessException(e);
		} catch (InvalidKeyException e) {
			throw new SettingsAccessException(e);
		}
	}
}
