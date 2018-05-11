package com.frozen_foo.shuffle_my_music_app.list_player;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.ListPlayerListener;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.PlayStateAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;

import java.io.File;
import java.util.List;

import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by Frank on 16.04.2018.
 */

public class Player {

	private static volatile boolean isShuttingDown;
	private static MediaPlayer mediaPlayer;
	private final ListPlayerListener listPlayerListener;
	private AudioManager audioManager;
	private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

	public Player(final ListPlayerListener listPlayerListener) {
		this.listPlayerListener = listPlayerListener;
	}

	public void appStart(Activity activity, ListView shuffleList) {
		initAudioInterruptionHandling(activity);
		prepareNextPlayer(activity);
		addItemSelectionListener(activity, shuffleList);
	}

	public void createNewListStarted(Activity activity) {
		stopAndResetPlayState(activity);
		listPlayerListener.onPause();
	}

	public void createNewListFinished(Activity activity) {
		prepareNextPlayer(activity);
	}

	public void userChangedSettings(Activity activity) {
		stopAndResetPlayState(activity);
		prepareNextPlayer(activity);
	}

	public void appShutDown(Activity activity) {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			synchronized (getClass()) {
				isShuttingDown = true;
				new PlayStateAccess(activity)
						.update(getSongIndexIfKnownOr0(activity), mediaPlayer.getCurrentPosition());
				mediaPlayer.stop();
				mediaPlayer.release();
			}
		}
	}

	public void playPause() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				listPlayerListener.onPause();
			} else {
				int permission = requestAudioFocus();
				if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == permission) {
					mediaPlayer.start();
					listPlayerListener.onStart();
				}
			}
		}
	}

	private int requestAudioFocus() {
		return audioManager
				.requestAudioFocus(onAudioFocusChangeListener, STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
	}

	public void playSong(Activity activity, int songIndex) {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		updatePlayState(activity, songIndex, 0);
		prepareNextPlayer(activity);
		playPause();
	}

	public void setVolumeMax(final boolean selected) {
	}

	private void initAudioInterruptionHandling(Activity activity) {
		audioManager = activity.getSystemService(AudioManager.class);
		onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
			@Override
			public void onAudioFocusChange(final int focusChange) {
				switch (focusChange) {
					case AudioManager.AUDIOFOCUS_LOSS:
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if (mediaPlayer != null) {
									mediaPlayer.pause();
								}
							}
						}, 30L);
						break;
					case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
						if (mediaPlayer != null) {
							mediaPlayer.pause();
						}
						break;
					case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
						break;
					case AudioManager.AUDIOFOCUS_GAIN:
						if (mediaPlayer != null) {
							mediaPlayer.start();
						}
						break;
				}
			}
		};
	}

	private void dismissAudioInterruptionHandling() {
		audioManager.abandonAudioFocus(onAudioFocusChangeListener);
	}

	private void stopAndResetPlayState(final Activity activity) {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}

		resetPlayState(activity);
	}

	private void prepareNextPlayer(final Activity activity) {
		final PlayState  playState    = recentPlayState(activity);
		List<IndexEntry> indexEntries = new ShuffleAccess().getLocalIndex(activity);
		final File[]     songs        = new ShuffleAccess().resolveLocalSongs(activity, indexEntries);

		if (songs.length > 0) {
			mediaPlayer = MediaPlayer.create(activity, Uri.fromFile(songs[playState.songIndex]));
			mediaPlayer.seekTo(playState.position);

			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(final MediaPlayer mp) {
					synchronized (Player.this.getClass()) {
						if (!isShuttingDown) {
							mediaPlayer.stop();
							mediaPlayer.release();
							updatePlayState(activity, nextSongIndex(activity, songs.length), 0);
							prepareNextPlayer(activity);
							mediaPlayer.start();
						}
					}
				}
			});
			listPlayerListener.playingSongChanged(playState.songIndex);
			mediaPlayer.setOnErrorListener(listPlayerListener);
		}
	}

	private void addItemSelectionListener(final Activity activity, ListView shuffleList) {
		shuffleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				playSong(activity, position);
			}
		});
	}

	private PlayState recentPlayState(Activity activity) {
		return new PlayState(getSongIndexIfKnownOr0(activity), getCurrentPositionIfKnownOr0(activity));
	}

	private int getCurrentPositionIfKnownOr0(Activity activity) {
		return new PlayStateAccess(activity).currentPosition(0);
	}

	private int getSongIndexIfKnownOr0(Activity activity) {
		return new PlayStateAccess(activity).songIndex(0);
	}

	private int nextSongIndex(Activity activity, int numberOfSongs) {
		int currentSongIndex = getSongIndexIfKnownOr0(activity);
		if (currentSongIndex < numberOfSongs - 1) {
			return currentSongIndex + 1;
		} else {
			return 0;
		}
	}

	private void resetPlayState(final Activity activity) {
		updatePlayState(activity, 0, 0);
	}

	private void updatePlayState(final Activity activity, final int songIndex, final int currentPosition) {
		new PlayStateAccess(activity).update(songIndex, currentPosition);
	}

	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		return false;
	}

	public boolean onKeyUp(final int keyCode, final KeyEvent event) {
		return false;
	}
}
