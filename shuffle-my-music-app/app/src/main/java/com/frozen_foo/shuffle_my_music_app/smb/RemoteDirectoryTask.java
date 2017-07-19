package com.frozen_foo.shuffle_my_music_app.smb;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.security.CryptoService;
import com.frozen_foo.shuffle_my_music_app.settings.Settings;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsService;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Frank on 14.07.2017.
 */

public class RemoteDirectoryTask extends AsyncTask<Context, Integer, String[]> {

	private Context context;

	private String[] listRemoteSongsDir(Context context) {
		Settings settings = new SettingsService().readSettings(context);
		try {
			CryptoService cryptoService = new CryptoService();
			String encryptedIp = settings.getIp();
			String encryptedName = settings.getUsername();
			String encryptedPassword = settings.getPassword();
			String shuffleMyMusicDir = settings.getMusicDir();
			if (StringUtils.isNoneEmpty(encryptedIp, encryptedName, encryptedPassword, shuffleMyMusicDir)) {
				return new SmbAccess().list(cryptoService.decrypt
								(encryptedIp), cryptoService.decrypt
								(encryptedName), cryptoService.decrypt(encryptedPassword),
						cryptoService.decrypt(shuffleMyMusicDir));
			} else {
				return new String[] {"NONE"};
			}
		} catch (Exception e) {
			return new String[] {e.getLocalizedMessage()};
		}
	}

	@Override
	protected String[] doInBackground(Context... params) {
		this.context = params[0];
		return listRemoteSongsDir(this.context);
	}

	@Override
	protected void onPostExecute(String[] strings) {
		super.onPostExecute(strings);
		Toast.makeText(context, Arrays.toString(strings), Toast.LENGTH_LONG).show();
	}
}
