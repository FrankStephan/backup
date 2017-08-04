package com.frozen_foo.shuffle_my_music_app.shuffle;

import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;

import java.io.InputStream;

/**
 * Created by Frank on 25.07.2017.
 */

public class ShuffleListTask extends AbstractAsyncTask<InputStream, Void, String[]> {

	public ShuffleListTask(AsyncCallback<String[]> callback) {
		super(callback);
	}

	@Override
	protected String[] doInBackground(InputStream... params) {
		return new ShuffleMyMusicService().randomIndexEntries(params[0], 10);
	}
}
