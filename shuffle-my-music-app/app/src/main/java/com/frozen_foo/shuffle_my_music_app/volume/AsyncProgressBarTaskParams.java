package com.frozen_foo.shuffle_my_music_app.volume;

import android.widget.ProgressBar;

/**
 * Created by Frank on 10.01.2018.
 */

public class AsyncProgressBarTaskParams {

	public final ProgressBar progressBar;
	public final int minTouchTimeMillis;
	public final int updateIntervalMillis;

	public AsyncProgressBarTaskParams(final ProgressBar progressBar, final int minTouchTimeMillis,
									  final int updateIntervalMillis) {
		this.progressBar = progressBar;
		this.minTouchTimeMillis = minTouchTimeMillis;
		this.updateIntervalMillis = updateIntervalMillis;
	}

}
