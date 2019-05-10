package com.frozen_foo.shuffle_my_music_app.settings;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Frank on 01.11.2017.
 */

public class SettingsIO {

	private static final String PREFERENCES_NAME = "prefs";

	private static final String IP = "ip";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String LOCAL_DIR = "shuffle_my_music_dir";
	private static final String REMOTE_DIR = "music_dir";

	public void writeSettings(Settings settings, Context context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(IP, settings.getIp());
		editor.putString(USERNAME, settings.getUsername());
		editor.putString(PASSWORD, settings.getPassword());
		editor.putString(LOCAL_DIR, settings.getLocalDir());
		editor.putString(REMOTE_DIR, settings.getRemoteDir());
		editor.commit();

		Map<String, ?> all = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getAll();
	}

	public Settings readSettings(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return new Settings(preferences.getString(IP, ""), preferences.getString(USERNAME, ""),
				preferences.getString(PASSWORD, ""), preferences.getString(LOCAL_DIR, ""),
				preferences.getString(REMOTE_DIR, ""));

	}
}
