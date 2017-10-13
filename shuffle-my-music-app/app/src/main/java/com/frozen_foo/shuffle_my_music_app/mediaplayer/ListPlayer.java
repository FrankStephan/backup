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
	private MediaPlayer.OnErrorListener onErrorListener;
	private Context context;

	private MediaPlayer currentPlayer;
	private int songIndex = 0;

	public ListPlayer(Context context, MediaPlayer.OnErrorListener onErrorListener) {
		this.context = context;
		this.onErrorListener = onErrorListener;
		loadSongs();
		initCurrentPlayer();
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
				onErrorListener.onError(currentPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, MediaPlayer.MEDIA_ERROR_IO);
			}
			currentPlayer.setOnErrorListener(onErrorListener);

			currentPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					startSongAtIndex(songIndex + 1);
				}
			});
		}
	}

	public boolean isPlaying() {
		return currentPlayer.isPlaying();
	}

	public void start() {
		currentPlayer.start();
	}

	public void pause() {
		currentPlayer.pause();
	}

	public void startSongAtIndex(int index) {
		release();
		songIndex = index;
		if (songIndex < songs.length - 1) {
			initCurrentPlayer();
			start();
		}
	}

	public void release() {
		currentPlayer.release();
	}
}
