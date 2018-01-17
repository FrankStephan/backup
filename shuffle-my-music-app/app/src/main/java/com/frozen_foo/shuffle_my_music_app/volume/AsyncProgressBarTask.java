package com.frozen_foo.shuffle_my_music_app.volume;

import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;

/**
 * Created by Frank on 10.01.2018.
 */

public class AsyncProgressBarTask extends AbstractAsyncTask<AsyncProgressBarTaskParams, Void, Boolean> {

	private AsyncProgressBarTaskParams p;

	public AsyncProgressBarTask(final AsyncCallback<Boolean> callback) {
		super(callback);
	}

	@Override
	protected Boolean doInBackground(AsyncProgressBarTaskParams... params) {
		AsyncProgressBarTaskParams p = params[0];
		p.progressBar.setMax(p.minTouchTimeMillis);
		p.setProgress(0);

		while (!isCancelled() && p.getProgress() < p.minTouchTimeMillis) {
			p.setProgress(p.getProgress() + p.updateIntervalMillis);
			try {
				Thread.sleep(p.updateIntervalMillis);
			} catch (InterruptedException e) {
				callback.setException(e);
			}
		}

		p.setProgress(0);

		boolean b = completedSuccessfully();
		return b;
	}

	private boolean completedSuccessfully() {
		return !isCancelled();
	}
}
