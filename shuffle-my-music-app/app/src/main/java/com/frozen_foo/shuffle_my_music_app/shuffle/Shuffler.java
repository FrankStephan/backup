package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Frank on 01.08.2017.
 */

public class Shuffler extends AbstractAsyncTask<NumberOfSongs, ShuffleProgress, File[]> {

	public Shuffler(AsyncCallback<File[]> callback) {
		super(callback);
	}

	@Override
	protected File[] doInBackground(NumberOfSongs... params) {
		try {
			return createNewShuffledList(params[0].contex, params[0].value);
		} catch (Exception e) {
			callback.setException(e);
			return new File[0];
		}

	}

	private File[] createNewShuffledList(Context context, int numberOfSongs) throws Exception {
		InputStream indexStream          = loadIndex(context);
		String[]    shuffledIndexEntries = shuffleIndexEntries(indexStream, numberOfSongs);
		return copySongsToLocalDir(context, shuffledIndexEntries);
	}

	private InputStream loadIndex(Context context) throws Exception {
		return new RemoteDirectoryAccess().indexStream(context);
	}

	private String[] shuffleIndexEntries(InputStream indexStream, int numberOfSongs) {
		return new ShuffleMyMusicService().randomIndexEntries(indexStream, numberOfSongs);
	}

	private File[] copySongsToLocalDir(Context context, String[] shuffledIndexEntries) throws Exception {
		RemoteDirectoryAccess remoteDirectoryAccess = new RemoteDirectoryAccess();
		LocalDirectoryAccess  localDirectoryAccess  = new LocalDirectoryAccess();
		for (String shuffledIndexEntry : shuffledIndexEntries) {
			InputStream remoteSongStream = remoteDirectoryAccess.songStream(context, shuffledIndexEntry);
			localDirectoryAccess.copyToLocal(remoteSongStream, shuffledIndexEntry);
		}
		return localDirectoryAccess.songs();
	}
}
