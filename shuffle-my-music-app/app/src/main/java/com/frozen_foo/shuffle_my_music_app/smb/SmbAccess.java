package com.frozen_foo.shuffle_my_music_app.smb;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

/**
 * Created by Frank on 12.07.2017.
 */

public class SmbAccess {

	public InputStream inputStream(String ip, String username, String password, String path) throws IOException {
		final SmbFile smbFile = smbFile(ip, username, password, path);
		boolean b = smbFile.exists();

		return smbFile.getInputStream();
	}

	@NonNull
	private SmbFile smbFile(String ip, String username, String password, String path) throws MalformedURLException {
		jcifs.Config.setProperty("jcifs.netbios.wins", ip);
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, password);
		return new SmbFile("smb://" + path, auth);
	}


}
