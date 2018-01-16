package com.frozen_foo.shuffle_my_music_app.volume;

import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;

/**
 * Created by Frank on 10.01.2018.
 */

public class AsyncProgressBarTask extends AbstractAsyncTask<AsyncProgressBarTaskParams, Void, Boolean> {



	public AsyncProgressBarTask(final AsyncCallback<Boolean> callback) {
		super(callback);
	}

	@Override
	protected Boolean doInBackground(AsyncProgressBarTaskParams... params) {
		AsyncProgressBarTaskParams p = params[0];
		p.progressBar.setMax(p.minTouchTimeMillis);
		p.progressBar.setProgress(0);

		while (!isCancelled() && p.progressBar.getProgress() < p.minTouchTimeMillis) {
			p.progressBar.setProgress(p.progressBar.getProgress() + p.updateIntervalMillis, true);
			try {
				Thread.sleep(p.updateIntervalMillis);
			} catch (InterruptedException e) {
				callback.setException(e);
			}
		}

		p.progressBar.setProgress(0);

		boolean b = completedSuccessfully();
		return b;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	private boolean completedSuccessfully() {
		return !isCancelled();
	}
}
