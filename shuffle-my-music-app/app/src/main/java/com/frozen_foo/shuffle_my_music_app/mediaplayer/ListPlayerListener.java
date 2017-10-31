package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.media.MediaPlayer;

/**
 * Created by Frank on 14.10.2017.
 */

public interface ListPlayerListener extends MediaPlayer.OnErrorListener, ListPlayerControllerListener {

	void onStart();
	void onPause();

}
