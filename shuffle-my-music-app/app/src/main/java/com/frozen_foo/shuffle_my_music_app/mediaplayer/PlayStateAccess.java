package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Frank on 15.04.2018.
 */

public class PlayStateAccess {

	private static final String PREFS_NAME = "playStateTempData";
	private static final String SONG_INDEX = "song_index";
	private static final String CURRENT_POSITION = "current_position";

	private final Context context;

	public PlayStateAccess(final Context context) {
		this.context = context;
	}

	public void update(int songIndex, int currentPosition) {
		sharedPreferences().edit().putInt(SONG_INDEX, songIndex).putInt(CURRENT_POSITION, currentPosition).apply();
	}

	public int songIndex(int defaultValue) {
		return sharedPreferences().getInt(SONG_INDEX, defaultValue);
	}

	public int currentPosition(int defaultValue) {
		return sharedPreferences().getInt(CURRENT_POSITION, defaultValue);
	}

	private SharedPreferences sharedPreferences() {
		return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}
}
