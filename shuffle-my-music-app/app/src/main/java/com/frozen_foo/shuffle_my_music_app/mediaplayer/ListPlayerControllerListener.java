package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;

/**
 * Created by Frank on 31.10.2017.
 */

public interface ListPlayerControllerListener {

	int NO_SONG = -1;

	void playingSongChanged(int index);
}
