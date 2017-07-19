package com.frozen_foo.shuffle_my_music_app.smb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.frozen_foo.shuffle_my_music_app.security.CryptoService;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import jcifs.smb.SmbSession;

/**
 * Created by Frank on 12.07.2017.
 */

public class SmbAccess {

	public String[] list(String ip, String username, String password, String path) throws
			CertificateException,
			NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException,
			InvalidAlgorithmParameterException, UnrecoverableEntryException,
			NoSuchPaddingException, InvalidKeyException, IOException {

		final SmbFile smbFile = smbFile(ip, username, password, path);
		return smbFile.list();
	}

	@NonNull
	private SmbFile smbFile(String ip, String username, String password, String path) throws MalformedURLException {
		jcifs.Config.setProperty("jcifs.netbios.wins", ip);
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, password);

		return new SmbFile("smb://" + path, auth);
	}


}
