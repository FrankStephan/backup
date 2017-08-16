package com.frozen_foo.shuffle_my_music_app.io.remote.smb;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.crypto.Cryptifier;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccess;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

/**
 * Created by Frank on 24.07.2017.
 */

public class IndexStreamTask extends AbstractAsyncTask<Context, Void, InputStream> {

	public static final String INDEX_FILE_NAME = "index.txt";

	public IndexStreamTask(AsyncCallback<InputStream> callback) {
		super(callback);
	}

	@Override
	protected InputStream doInBackground(Context... params) {
		Settings settings = new SettingsAccess().readSettings(params[0]);
		try {
			Cryptifier cryptifier        = new Cryptifier();
			String     encryptedIp       = settings.getIp();
			String     encryptedName     = settings.getUsername();
			String     encryptedPassword = settings.getPassword();
			String     musicDir          = settings.getMusicDir();
			if (StringUtils.isNoneEmpty(encryptedIp, encryptedName, encryptedPassword, musicDir)) {
				InputStream inputStream = new SmbAccess()
						.inputStream(cryptifier.decrypt(encryptedIp), cryptifier.decrypt(encryptedName),
								cryptifier.decrypt(encryptedPassword), indexPath(cryptifier.decrypt(musicDir)));
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
