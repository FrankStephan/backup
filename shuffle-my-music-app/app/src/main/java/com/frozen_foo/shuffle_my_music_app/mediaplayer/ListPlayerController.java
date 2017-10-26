package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.R;

/**
 * Created by Frank on 04.08.2017.
 */

public class ListPlayerController {

	private ListPlayer listPlayer;

	public void initPlayer(final Context context, ListView shuffleList, final MenuItem playItem) {
		release();

		listPlayer = new ListPlayer(context, new ListPlayerListener() {

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
				Toast.makeText(context, "MediaPlayer Error " + what + " " + extra, Toast.LENGTH_LONG);
				return false;
			}
		});

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

	public void release() {
		if (listPlayer != null) {
			if (listPlayer.isPlaying()) {
				listPlayer.pause();
			}
			listPlayer.release();
		}
	}
}
