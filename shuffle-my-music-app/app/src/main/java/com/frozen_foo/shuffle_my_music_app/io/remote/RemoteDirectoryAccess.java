package com.frozen_foo.shuffle_my_music_app.io.remote;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_app.io.remote.smb.SmbAccess;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.NoSuchPaddingException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

/**
 * Created by Frank on 01.08.2017.
 */

public class RemoteDirectoryAccess {

	public static final String INDEX_FILE_NAME = "index.txt";
	public static final String FAVORITES_FILE_NAME = "favorites.xml";

	public InputStream indexStream(Context context) throws SettingsAccessException, IOException {
		return inStream(context, INDEX_FILE_NAME);
	}

	public InputStream songStream(Context context, String indexEntry) throws SettingsAccessException, IOException {
		return inStream(context, indexEntry);
	}

	public InputStream favoritesFileInStream(Context context) throws SettingsAccessException, IOException {
		return inStream(context, FAVORITES_FILE_NAME);
	}

	public OutputStream favoritesFileOutStream(Context context) throws SettingsAccessException, IOException {
		return outStream(context, FAVORITES_FILE_NAME);
	}

	private InputStream inStream(Context context, String subPath) throws SettingsAccessException, IOException {
		Settings settings = new SettingsAccess().readSettings(context);
		String   path     = concat(settings.getRemoteDir(), subPath);
		return new SmbAccess().inputStream(settings.getIp(), settings.getUsername(), settings.getPassword(), path);
	}

	private OutputStream outStream(Context context, String subPath) throws SettingsAccessException, IOException {
		Settings settings = new SettingsAccess().readSettings(context);
		String   path     = concat(settings.getRemoteDir(), subPath);
		return new SmbAccess().outputStream(settings.getIp(), settings.getUsername(), settings.getPassword(), path);
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
