package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;

/**
 * Created by Frank on 04.08.2017.
 */

public class ListPlayerController extends AbstractListController {

	private ListPlayer listPlayer;

	public void initPlayer(final Activity activity, ListView shuffleList, final MenuItem playItem,
						   final ListPlayerControllerListener listPlayerControllerListener) {
		release();

		listPlayer = new ListPlayer(activity, new ListPlayerListener() {

			@Override
			public void onStart() {
				playItem.setIcon(R.drawable.ic_pause_black_24dp);
			}

			@Override
			public void onPause() {
				playItem.setIcon(R.drawable.ic_play_arrow_black_24dp);
			}

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(activity, "MediaPlayer Error " + what + " " + extra, Toast.LENGTH_LONG).show();
				return false;
			}

			@Override
			public void playingSongChanged(final int index) {
				listPlayerControllerListener.playingSongChanged(index);
			}

			@Override
			public void loadingSongsFailed(final Exception e) {
				alertException(activity, e);
			}
		});

		reloadSongs();

		addItemSelectionListener(shuffleList);
	}

	private void addItemSelectionListener(ListView shuffleList) {
		shuffleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				listPlayer.startSongAtIndex(position);
			}
		});
	}

	public void playPause() {
		if (listPlayer != null) {
			if (listPlayer.isPlaying()) {
				listPlayer.pause();
			} else {
				listPlayer.start();
			}
		}
	}

	public boolean onKeyUp(final int keyCode, final KeyEvent event) {
		if (listPlayer != null) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_MEDIA_PLAY:
				case KeyEvent.KEYCODE_MEDIA_PAUSE:
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
					playPause();
					return true;
				case KeyEvent.KEYCODE_MEDIA_NEXT:
					listPlayer.nextSong();
					return true;
				case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
					listPlayer.previousSong();
					return true;
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (listPlayer != null) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_MEDIA_PLAY:
				case KeyEvent.KEYCODE_MEDIA_PAUSE:
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
				case KeyEvent.KEYCODE_MEDIA_NEXT:
				case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
					return true;
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	public void release() {
		if (listPlayer != null) {
			if (listPlayer.isPlaying()) {
				listPlayer.pause();
			}
			listPlayer.release();
		}
	}

	public void reloadSongs() {
		listPlayer.loadSongs();
	}

	public int[] getDurations() {
		return listPlayer.getDurations();
	}

	public void setVolumeMax(boolean volumeMax) {
		if (true == volumeMax) {
			if (listPlayer != null) {
				listPlayer.volumeMax();
			}
		} else {

		}
	}
}
