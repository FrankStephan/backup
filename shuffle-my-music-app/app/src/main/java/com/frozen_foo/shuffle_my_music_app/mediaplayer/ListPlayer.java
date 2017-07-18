package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Frank on 18.07.2017.
 */

public class ListPlayer {

	private File[] songs;
	private MediaPlayer.OnErrorListener onErrorListener;
	private Context context;

	private MediaPlayer currentPlayer;
	private int songIndex = 0;

	public ListPlayer(Context contex, final File[] songs, MediaPlayer.OnErrorListener onErrorListener) {
		this.context = contex;
		this.songs = songs;
		this.onErrorListener = onErrorListener;
		initCurrentPlayer();
	}

	private void initCurrentPlayer()  {
		currentPlayer = new MediaPlayer();
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
				startSongAtIndex(songIndex+1);
			}
		});
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
		if (songIndex<songs.length-1) {
			initCurrentPlayer();
			start();
		}
	}

	public void release() {
		currentPlayer.release();
	}
}
