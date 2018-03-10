package com.frozen_foo.shuffle_my_music_app.shuffle.progress;

import android.os.Handler;

import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

/**
 * Created by Frank on 06.03.2018.
 */

public class ShuffleProgressRunner {

	public void runSync(ShuffleProgress shuffleProgress, int numberOfSongs, ShuffleProgressRunnable runnable) {
		runnable.run(shuffleProgress, numberOfSongs);
	}

	public void runAsync(final ShuffleProgress shuffleProgress, final int numberOfSongs, final ShuffleProgressRunnable runnable, Handler handler) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				runnable.run(shuffleProgress, numberOfSongs);
			}
		});
	}


}
