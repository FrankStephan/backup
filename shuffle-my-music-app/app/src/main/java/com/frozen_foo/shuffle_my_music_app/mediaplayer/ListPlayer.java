package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Frank on 18.07.2017.
 */

public class ListPlayer {

	AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;
	private File[] songs;
	private ListPlayerListener listPlayerListener;
	private Context context;
	private AudioManager audioManager;
	private MediaPlayer currentPlayer;
	private int songIndex = 0;

	public ListPlayer(Context context, ListPlayerListener listPlayerListener) {
		this.context = context;
		this.listPlayerListener = listPlayerListener;
	}

	public boolean isPlaying() {
		return currentPlayer != null && currentPlayer.isPlaying();
	}

	public void start() {
		init();
		int permission = requestAudioFocus();
		if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == permission) {
			currentPlayer.start();
			listPlayerListener.onStart();
		}
	}

	private int requestAudioFocus() {
		return audioManager
				.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
	}

	public void pause() {
		init();
		currentPlayer.pause();
		listPlayerListener.onPause();
	}

	public void startSongAtIndex(int index) {
		if (index < songs.length) {
			songIndex = index;
			if (currentPlayer == null) {
				init();
				start();
			} else {
				currentPlayer.stop();
				currentPlayer.release();
				initCurrentPlayer();
				start();
			}
		}
	}

	public void release() {
		if (currentPlayer != null) {
			currentPlayer.stop();
			currentPlayer.release();
			currentPlayer = null;
			songs = null;
			listPlayerListener.playingSongChanged(ListPlayerControllerListener.NO_SONG);
			audioManager.abandonAudioFocus(onAudioFocusChangeListener);
		}
	}

	private void init() {
		if (currentPlayer == null) {
			audioManager = context.getSystemService(AudioManager.class);
			onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
				@Override
				public void onAudioFocusChange(final int focusChange) {
					switch (focusChange) {
						case AudioManager.AUDIOFOCUS_LOSS:
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									pause();
								}
							}, 30L);
							break;
						case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
							pause();
							break;
						case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
							break;
						case AudioManager.AUDIOFOCUS_GAIN:
							start();
							break;
					}
				}
			};
			initCurrentPlayer();
		}
	}

	public void loadSongs() {
		List<IndexEntry> indexEntries = null;
		try {
			indexEntries = new ShuffleAccess().getLocalIndex(context);
		} catch (SettingsAccessException e) {
			listPlayerListener.loadingSongsFailed(e);
		}
		try {
			songs = new ShuffleAccess().resolveLocalSongs(context, indexEntries);
		} catch (SettingsAccessException e) {
			listPlayerListener.loadingSongsFailed(e);
		}

	}

	private void initCurrentPlayer() {
		if (!ArrayUtils.isEmpty(songs)) {
			currentPlayer = MediaPlayer.create(context, Uri.fromFile(songs[songIndex]));
			currentPlayer.setOnErrorListener(listPlayerListener);
			currentPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					startSongAtIndex(songIndex + 1);
				}
			});
			listPlayerListener.playingSongChanged(songIndex);
		}
	}
}
