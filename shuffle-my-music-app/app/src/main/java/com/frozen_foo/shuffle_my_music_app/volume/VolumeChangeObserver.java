package com.frozen_foo.shuffle_my_music_app.volume;

import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

/**
 * Created by Frank on 15.01.2018.
 */

public class VolumeChangeObserver extends ContentObserver {

	private final AudioManager audioManager;
	private final VolumeChangeListener volumeChangeListener;

	private int previousVolume;

	/**
	 * Creates a content observer.
	 *
	 * @param handler The handler to run {@link #onChange} on, or null if none.
	 */
	public VolumeChangeObserver(final Handler handler, AudioManager audioManager, VolumeChangeListener volumeChangeListener) {
		super(handler);
		this.audioManager = audioManager;
		this.volumeChangeListener = volumeChangeListener;
		previousVolume = volume();
	}

	@Override
	public void onChange(final boolean selfChange) {
		super.onChange(selfChange);
		int currentVolume = volume();
		if (previousVolume != currentVolume) {
			previousVolume = currentVolume;
			volumeChangeListener.volumeChanged();
		}
	}

	private int volume() {
		return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}
}
