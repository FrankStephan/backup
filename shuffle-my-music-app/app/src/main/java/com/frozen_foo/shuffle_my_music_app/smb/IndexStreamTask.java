package com.frozen_foo.shuffle_my_music_app.smb;

import android.content.Context;
import android.os.AsyncTask;

import com.frozen_foo.shuffle_my_music_app.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.security.CryptoService;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsService;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

/**
 * Created by Frank on 24.07.2017.
 */

public class IndexStreamTask extends AsyncTask<Context, Void, InputStream> {

	public static final String INDEX_FILE_NAME = "index.txt";
	private AsyncCallback<InputStream> callback;

	public IndexStreamTask(AsyncCallback<InputStream> callback) {
		this.callback = callback;
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
				return new SmbAccess().inputStream(cryptoService.decrypt
								(encryptedIp), cryptoService.decrypt
								(encryptedName), cryptoService.decrypt(encryptedPassword),
						indexPath(cryptoService.decrypt(musicDir)));
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

	@Override
	protected void onPostExecute(InputStream inputStream) {
		callback.invoke(inputStream);
	}
}
