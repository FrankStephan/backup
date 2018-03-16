package com.frozen_foo.shuffle_my_music_app.durations;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;

import java.io.File;
import java.util.List;

/**
 * Created by Frank on 12.03.2018.
 */

public class DurationsAccess {

	private static final String PREFS_NAME = "durationsData";

	private final Context context;

	public DurationsAccess(final Context context) {
		this.context = context;
	}

	public void clear() {
		sharedPreferences().edit().clear().apply();
	}

	public int duration(int songIndex, int defaultDuration) {
		return sharedPreferences().getInt(Integer.toString(songIndex), defaultDuration);
	}

	public void updateForSong(int songIndex) {
		File[] localSongs = localSongs();
		int duration = calculateDuration(localSongs[songIndex]);
		sharedPreferences().edit().putInt(String.valueOf(songIndex), duration).apply();
	}

	public void updateForAllSongs() {
		File[] localSongs = localSongs();
		SharedPreferences.Editor editor  = sharedPreferences().edit();
		for (int songIndex = 0; songIndex < localSongs.length; songIndex++) {
			int duration = calculateDuration(localSongs[songIndex]);
			editor.putInt(String.valueOf(songIndex), duration);
		}
		editor.apply();
	}

	private File[] localSongs() {
		ShuffleAccess    shuffleAccess = new ShuffleAccess();
		List<IndexEntry> localIndex    = shuffleAccess.getLocalIndex(context);
		return shuffleAccess.resolveLocalSongs(context, localIndex);
	}

	private int calculateDuration(File localSong) {
		if (localSong.exists()) {
			MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.fromFile(localSong));
			if (mediaPlayer != null) {
				int duration = mediaPlayer.getDuration();
				mediaPlayer.release();
				return duration;
			} else {
				// Song file seems to be corrupted
				return -1;
			}
		} else {
			return 0;
		}
	}

	private SharedPreferences sharedPreferences() {
		return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}
}
