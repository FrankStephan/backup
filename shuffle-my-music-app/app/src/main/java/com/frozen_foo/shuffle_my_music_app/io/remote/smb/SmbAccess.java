package com.frozen_foo.shuffle_my_music_app.io.remote.smb;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Created by Frank on 12.07.2017.
 */

public class SmbAccess {

	public boolean exists(String ip, String username, String password, String path) throws IOException {
		return smbFile(ip, username, password, path).exists();
	}

	public InputStream inputStream(String ip, String username, String password, String path) throws IOException {
		final SmbFile smbFile = smbFile(ip, username, password, path);
		return smbFile.getInputStream();
	}

	public OutputStream outputStream(String ip, String username, String password, String path) throws IOException {
		final SmbFile smbFile = smbFile(ip, username, password, path);
		return smbFile.getOutputStream();
	}

	@NonNull
	private SmbFile smbFile(String ip, String username, String password, String path) throws MalformedURLException,
			SmbException {
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, password);
		SmbFile                    smbFile = new SmbFile("smb://" + path, auth);
		if (!smbFile.exists()) {
			smbFile.createNewFile();
		}
		return smbFile;
	}
}
