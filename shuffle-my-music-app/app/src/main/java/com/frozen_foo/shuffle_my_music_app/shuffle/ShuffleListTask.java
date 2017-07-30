package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;
import android.os.AsyncTask;

import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.base.AbstractAsyncTask;

import java.io.InputStream;

/**
 * Created by Frank on 25.07.2017.
 */

public class ShuffleListTask extends AbstractAsyncTask<InputStream, String[]> {

	public ShuffleListTask(AsyncCallback<String[]> callback) {
		super(callback);
	}

	@Override
	protected String[] doInBackground(InputStream... params) {
		return new ShuffleMyMusicService().randomIndexEntries(params[0], 10);
	}
}
