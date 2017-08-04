package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.MenuItem;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.R;

import java.io.File;

/**
 * Created by Frank on 04.08.2017.
 */

public class ListPlayerController {

	private ListPlayer listPlayer;

	private Application application;
	private Context context;

	public ListPlayerController(final Application application, final Context context) {
		this.application = application;
		this.context = context;
	}

	public void loadPlayer(File[] songs) {
		release();
		listPlayer = new ListPlayer(application, songs, new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(context, "MediaPlayer Error " + what + " " + extra, Toast.LENGTH_LONG);
				return false;
			}
		});
	}

	public void playPause(MenuItem menuItem) {
		if (listPlayer != null) {
			if (listPlayer.isPlaying()) {
				menuItem.setIcon(R.drawable.ic_play_arrow_black_24dp);
				listPlayer.pause();
			} else {
				menuItem.setIcon(R.drawable.ic_pause_black_24dp);
				listPlayer.start();
			}
		}
	}

	public void release() {
		if (listPlayer != null) {
			listPlayer.release();
		}
	}
}
