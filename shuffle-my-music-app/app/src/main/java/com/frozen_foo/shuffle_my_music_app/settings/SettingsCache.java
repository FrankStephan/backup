package com.frozen_foo.shuffle_my_music_app.settings;

/**
 * Created by Frank on 05.11.2017.
 */

public class SettingsCache {

	private boolean isValid;

	private Settings settings;

	public boolean isValid() {
		return isValid;
	}

	public void setValid(final boolean valid) {
		isValid = valid;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(final Settings settings) {
		this.settings = settings;
	}
}
