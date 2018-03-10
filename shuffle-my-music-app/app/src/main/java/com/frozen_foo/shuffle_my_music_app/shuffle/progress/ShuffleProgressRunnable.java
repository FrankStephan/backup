package com.frozen_foo.shuffle_my_music_app.shuffle.progress;

import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

/**
 * Created by Frank on 24.02.2018.
 */

public interface ShuffleProgressRunnable {

	void run(final ShuffleProgress shuffleProgress, final int numberOfSongs);
}
