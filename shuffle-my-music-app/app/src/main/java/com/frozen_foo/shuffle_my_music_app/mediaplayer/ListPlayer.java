package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Frank on 18.07.2017.
 */

public class ListPlayer {

	private File[] songs;
	private ListPlayerListener listPlayerListener;
	private Context context;

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
		currentPlayer.start();
		listPlayerListener.onStart();
	}

	public void pause() {
		init();
		currentPlayer.pause();
		listPlayerListener.onPause();
	}

	public void startSongAtIndex(int index) {
		if (currentPlayer == null) {
			loadSongs();
		} else {
			release();
		}
		if (index < songs.length) {
			songIndex = index;
			initCurrentPlayer();
			start();
		}
	}

	public void release() {
		if (currentPlayer != null) {
			currentPlayer.release();
		}
	}

	private void init() {
		if (currentPlayer == null) {
			loadSongs();
			initCurrentPlayer();
		}
	}

	private void loadSongs() {
		List<IndexEntry> indexEntries = new ShuffleAccess().getLocalIndex();
		songs = new File[indexEntries.size()];
		for (int i = 0; i < songs.length; i++) {
			songs[i] = new ShuffleAccess().resolveLocalSong(indexEntries.get(i));
		}
	}

	private void initCurrentPlayer() {
		currentPlayer = new MediaPlayer();
		if (!ArrayUtils.isEmpty(songs)) {
			currentPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
				currentPlayer.setDataSource(context, Uri.fromFile(songs[songIndex]));
				currentPlayer.prepare();
			} catch (IOException e) {
				listPlayerListener.onError(currentPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, MediaPlayer.MEDIA_ERROR_IO);
			}
			currentPlayer.setOnErrorListener(listPlayerListener);

			currentPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					startSongAtIndex(songIndex + 1);
				}
			});
		}
	}
}
