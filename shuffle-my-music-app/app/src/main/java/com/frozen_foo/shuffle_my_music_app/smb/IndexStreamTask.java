package com.frozen_foo.shuffle_my_music_app.smb;

import android.content.Context;
import android.os.AsyncTask;

import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.base.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.crypto.CryptoService;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

import dalvik.system.DexClassLoader;

/**
 * Created by Frank on 24.07.2017.
 */

public class IndexStreamTask extends AbstractAsyncTask<Context, InputStream> {

	public static final String INDEX_FILE_NAME = "index.txt";

	public IndexStreamTask(AsyncCallback<InputStream> callback) {
		super(callback);
	}

	@Override
	protected InputStream doInBackground(Context... params) {
		Settings settings = new SettingsService().readSettings(params[0]);
		try {
			CryptoService cryptoService = new CryptoService();
			String encryptedIp = settings.getIp();
			String encryptedName = settings.getUsername();
			String encryptedPassword = settings.getPassword();
			String musicDir = settings.getMusicDir();
			if (StringUtils.isNoneEmpty(encryptedIp, encryptedName, encryptedPassword, musicDir)) {
				InputStream inputStream = new SmbAccess().inputStream(cryptoService.decrypt
								(encryptedIp), cryptoService.decrypt
								(encryptedName), cryptoService.decrypt(encryptedPassword),
						indexPath(cryptoService.decrypt(musicDir)));

				String s = IOUtils.toString(inputStream, "UTF-8");

//				Groovy compiler option: siehe Kapitel 7 in http://groovy-lang.org/groovyc.html

				String[] strings = new ShuffleMyMusicService().randomIndexEntries(IOUtils.toInputStream(s, "UTF-8"), 2);
				return inputStream;
			} else {
				callback.setException(new IllegalArgumentException("Settings fehlen."));
				return null;
			}
		} catch (Exception e) {
			callback.setException(e);
			return null;
		}
	}

	private String indexPath(String musicDir) {
		return musicDir + INDEX_FILE_NAME;
	}
}
