package com.frozen_foo.shuffle_my_music_app.settings;

/**
 * Created by Frank on 01.11.2017.
 */

public class SettingsAccessException extends Exception {

	public SettingsAccessException(final Throwable cause) {
		super("Failed to access settings ", cause);
	}
}
