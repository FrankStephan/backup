package com.frozen_foo.shuffle_my_music_app.volume;

import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Frank on 14.01.2018.
 */

public class LongTouchProcessor {

	private AsyncProgressBarTask asyncProgressBarTask;

	public void process(int action, int minTouchTimeMillis, int updateIntervalMillis, ProgressBar progressBar, AsyncCallback<Boolean> callback) {
		switch (action) {
			case ACTION_DOWN:
				startTouchDownTimer(minTouchTimeMillis, updateIntervalMillis, progressBar, callback);
				break;
			case ACTION_MOVE:
				break;
			case ACTION_UP:
				interruptTouchDownTimer();
				break;
		}
	}

	private void startTouchDownTimer(final int minTouchTimeMillis, final int updateIntervalMillis,
									 final ProgressBar progressBar, final AsyncCallback<Boolean> callback) {
		asyncProgressBarTask = new AsyncProgressBarTask(callback);
		asyncProgressBarTask
				.execute(new AsyncProgressBarTaskParams(progressBar, minTouchTimeMillis, updateIntervalMillis));
	}

	private void interruptTouchDownTimer() {
		if (asyncProgressBarTask != null) {
			asyncProgressBarTask.cancel(false);
		}
	}
}
