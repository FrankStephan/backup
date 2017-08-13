package com.frozen_foo.shuffle_my_music_app.list.create;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.list.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.list.NumberOfSongs;
import com.frozen_foo.shuffle_my_music_app.list.RemoteDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.DeterminedSongsStep;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.StartSongCopyStep;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Frank on 01.08.2017.
 */

public class Shuffler extends AbstractAsyncTask<NumberOfSongs, ShuffleProgress, File[]> {

	public static final int NUMBER_OF_PREPARATION_STEPS = 2;

	public Shuffler(AsyncCallback<File[]> callback, ProgressMonitor<ShuffleProgress> progressMonitor) {
		super(callback, progressMonitor);
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
		publishProgress(PreparationStep.LOADING_INDEX);
		InputStream indexStream          = loadIndex(context);
		publishProgress(PreparationStep.SHUFFLING_INDEX);
		IndexEntry[]    shuffledIndexEntries = shuffleIndexEntries(indexStream, numberOfSongs);
		String[] fileNames = new String[shuffledIndexEntries.length];
		for (int i = 0; i < shuffledIndexEntries.length; i++) {
			fileNames[i] = shuffledIndexEntries[i].getFileName();
		}

		publishProgress(new DeterminedSongsStep(fileNames));
		return copySongsToLocalDir(context, shuffledIndexEntries);
	}

	private InputStream loadIndex(Context context) throws Exception {
		return new RemoteDirectoryAccess().indexStream(context);
	}

	private IndexEntry[] shuffleIndexEntries(InputStream indexStream, int numberOfSongs) {
		return new ShuffleMyMusicService().randomIndexEntries(indexStream, numberOfSongs);
	}

	private File[] copySongsToLocalDir(Context context, IndexEntry[] shuffledIndexEntries) throws Exception {
		RemoteDirectoryAccess remoteDirectoryAccess = new RemoteDirectoryAccess();
		LocalDirectoryAccess  localDirectoryAccess  = new LocalDirectoryAccess();
		localDirectoryAccess.cleanLocalDir();
		for (int i = 0; i < shuffledIndexEntries.length; i++) {
			publishProgress(new StartSongCopyStep(i));
			InputStream remoteSongStream = remoteDirectoryAccess.songStream(context, shuffledIndexEntries[i].getPath());
			localDirectoryAccess.copyToLocal(remoteSongStream, shuffledIndexEntries[i].getFileName());
			publishProgress(new FinishedSongCopyStep(i));
		}
		publishProgress(new FinalizationStep());
		return localDirectoryAccess.songs();
	}
}
