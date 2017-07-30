package com.frozen_foo.shuffle_my_music_app.base;

import android.content.Context;
import android.os.AsyncTask;

import com.frozen_foo.shuffle_my_music_app.AsyncCallback;

/**
 * Created by Frank on 25.07.2017.
 */

public abstract class AbstractAsyncTask<Params, Result> extends AsyncTask<Params, Integer, Result> {

	protected AsyncCallback<Result> callback;

	public AbstractAsyncTask(AsyncCallback<Result> callback) {
		this.callback = callback;
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		callback.invoke(result);
	}
}
