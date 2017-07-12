package com.frozen_foo.shuffle_my_music_app.settings;

import android.content.SharedPreferences;

/**
 * Created by Frank on 12.07.2017.
 */

public class SettingsService {

	private static final String IP = "ip";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	public void writeSettings(Settings settings, SharedPreferences preferences) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(IP, settings.getIp());
		editor.putString(USERNAME, settings.getUsername());
		editor.putString(PASSWORD, settings.getPassword());
		editor.commit();
	}

	public Settings readSettings(SharedPreferences preferences) {
		return new Settings(preferences.getString(IP, ""),
				preferences.getString(USERNAME, ""),
				preferences.getString(PASSWORD, ""));
	}
}
