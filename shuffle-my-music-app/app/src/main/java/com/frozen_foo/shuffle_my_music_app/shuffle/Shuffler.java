package com.frozen_foo.shuffle_my_music_app.shuffle;

import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;

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
		return createNewShuffledList(params[0].value);
	}

	private File[] createNewShuffledList(int numberOfSongs) {
		InputStream indexStream = loadIndex();
		String[] shuffledIndexEntries = shuffleIndexEntries(indexStream, numberOfSongs);
		return copySongsToLocalDir(shuffledIndexEntries);
	}

	private InputStream loadIndex() {
		return new RemoteDirectoryAccess().indexStream();
	}

	private String[] shuffleIndexEntries(InputStream indexStream, int numberOfSongs) {
		return new ShuffleMyMusicService().randomIndexEntries(indexStream, numberOfSongs);
	}

	private File[] copySongsToLocalDir(String[] shuffledIndexEntries) {
		RemoteDirectoryAccess remoteDirectoryAccess = new RemoteDirectoryAccess();
		LocalDirectoryAccess localDirectoryAccess = new LocalDirectoryAccess();
		for (String shuffledIndexEntry : shuffledIndexEntries) {
			InputStream remoteSongStream = remoteDirectoryAccess.songStream(shuffledIndexEntry);
			localDirectoryAccess.copyToLocal(remoteSongStream);
		}
		return localDirectoryAccess.songs();
	}
}
