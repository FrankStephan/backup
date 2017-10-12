package com.frozen_foo.shuffle_my_music_app.async;

import android.os.AsyncTask;

/**
 * Created by Frank on 25.07.2017.
 */

public abstract class AbstractAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	protected AsyncCallback<Result> callback;
	protected ProgressMonitor<Progress> progressMonitor;

	public AbstractAsyncTask(AsyncCallback<Result> callback) {
		this(callback, null);
	}

	public AbstractAsyncTask(AsyncCallback<Result> callback, ProgressMonitor<Progress> progressMonitor) {
		this.callback = callback;
		this.progressMonitor = progressMonitor;
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
		if (progressMonitor != null) {
			progressMonitor.updateProgress(values[0]);
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		callback.invoke(result);
	}
}
