package com.frozen_foo.shuffle_my_music_app.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Frank on 12.07.2017.
 */

public class SettingsService {

	private static final String PREFERENCES_NAME = "prefs";

	private static final String IP = "ip";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String SHUFFLE_MY_MUSIC_DIR = "shuffle_my_music_dir";

	public void writeSettings(Settings settings, Context context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context
				.MODE_PRIVATE).edit();
		editor.putString(IP, settings.getIp());
		editor.putString(USERNAME, settings.getUsername());
		editor.putString(PASSWORD, settings.getPassword());
		editor.putString(SHUFFLE_MY_MUSIC_DIR, settings.getShuffleMyMusicDir());
		editor.commit();
	}

	public Settings readSettings(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		return new Settings(preferences.getString(IP, ""),
				preferences.getString(USERNAME, ""),
				preferences.getString(PASSWORD, ""),
				preferences.getString(SHUFFLE_MY_MUSIC_DIR, ""));
	}
}
