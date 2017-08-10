package com.frozen_foo.shuffle_my_music_app.list;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_app.crypto.Cryptifier;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccess;
import com.frozen_foo.shuffle_my_music_app.smb.SmbAccess;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by Frank on 01.08.2017.
 */

public class RemoteDirectoryAccess {

	public static final String INDEX_FILE_NAME = "index.txt";

	public InputStream indexStream(Context context) throws CertificateException, NoSuchAlgorithmException,
			KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException,
			UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException {
		return stream(context, INDEX_FILE_NAME);
	}

	public InputStream songStream(Context context, String indexEntry) throws IOException, CertificateException,
			NoSuchAlgorithmException, InvalidKeyException, UnrecoverableEntryException,
			InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchProviderException, KeyStoreException {
		return stream(context, indexEntry);
	}

	private InputStream stream(Context context, String subPath) throws CertificateException, NoSuchAlgorithmException,
			KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException,
			UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException {
		Settings settings          = new SettingsAccess().readSettings(context);
		String   encryptedIp       = settings.getIp();
		String   encryptedName     = settings.getUsername();
		String   encryptedPassword = settings.getPassword();
		String   encryptedMusicDir = settings.getMusicDir();
		if (StringUtils.isNoneEmpty(encryptedIp, encryptedName, encryptedPassword, encryptedMusicDir)) {
			Cryptifier cryptifier = new Cryptifier();
			String     ip         = cryptifier.decrypt(encryptedIp);
			String     username   = cryptifier.decrypt(encryptedName);
			String     password   = cryptifier.decrypt(encryptedPassword);
			String     musicDir   = cryptifier.decrypt(encryptedMusicDir);

			String      path        = concat(musicDir, subPath);
			InputStream inputStream = new SmbAccess().inputStream(ip, username, password, path);
			return inputStream;
		} else {
			throw new IllegalArgumentException("Bitte Settings ausfüllen.");
		}
	}

	private String concat(String pathSegment1, String pathSegment2) {
		if (pathSegment1.endsWith("/")) {
			if (pathSegment2.startsWith("/")) {
				return pathSegment1 + pathSegment2.substring(1);
			} else {
				return pathSegment1 + pathSegment2;
			}
		} else {
			if (pathSegment2.startsWith("/")) {
				return pathSegment1 + pathSegment2;
			} else {
				return pathSegment1 + "/" + pathSegment2;
			}
		}
	}
}
