package com.frozen_foo.shuffle_my_music_app.local;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Frank on 15.07.2017.
 */

public class LocalDirectoryTask extends AsyncTask<ListView, Integer, String[]> {

	private static String SHUFFLE_MY_MUSIC_FOLDER = "_shuffle-my-music";
	private ListView shuffleList;

	@Override
	protected String[] doInBackground(ListView... params) {
		this.shuffleList = params[0];
		File shuffleMyMusicDir = new File(Environment.getExternalStorageDirectory(), SHUFFLE_MY_MUSIC_FOLDER);
		if (shuffleMyMusicDir.exists()) {
			String[] list = shuffleMyMusicDir.list();

			return list;
		} else {
			return new String[] {"NONE"};
		}
	}

	@Override
	protected void onPostExecute(String[] strings) {
		super.onPostExecute(strings);
		Toast.makeText(shuffleList.getContext(), Arrays.toString(strings), Toast.LENGTH_LONG).show();
	}
}
