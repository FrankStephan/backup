package com.frozen_foo.shuffle_my_music_app.shuffle.progress;

import android.content.Context;
import android.os.Handler;

import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

/**
 * Created by Frank on 06.03.2018.
 */

public class ShuffleProgressProcessor {

	public void processUpdateWithMostRecentProgressAsync(Context context, Handler handler,
														 final ShuffleProgressRunnable runnable) {
		synchronized (getClass()) {
			ShuffleProgressAccess shuffleProgressAccess = new ShuffleProgressAccess(context);
			ShuffleProgress       shuffleProgress       = shuffleProgressAccess.mostRecentProgress();
			int                   numberOfSongs         = shuffleProgressAccess.numberOfSongs();
			new ShuffleProgressRunner().runAsync(shuffleProgress, numberOfSongs, runnable, handler);
		}
	}

	public void processUpdateWithMostRecentProgressSync(Context context, final ShuffleProgressRunnable runnable) {
		synchronized (getClass()) {
			ShuffleProgressAccess shuffleProgressAccess = new ShuffleProgressAccess(context);
			ShuffleProgress       shuffleProgress       = shuffleProgressAccess.mostRecentProgress();
			int                   numberOfSongs         = shuffleProgressAccess.numberOfSongs();
			new ShuffleProgressRunner().runSync(shuffleProgress, numberOfSongs, runnable);
		}
	}
}
