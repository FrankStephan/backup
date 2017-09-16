package com.frozen_foo.shuffle_my_music_app.main.create_list;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.io.remote.RemoteDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.DeterminedSongsStep;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.StartSongCopyStep;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Frank on 01.08.2017.
 */

public class CreateListTask extends AbstractAsyncTask<NumberOfSongs, ShuffleProgress, IndexEntry[]> {

	public static final int NUMBER_OF_PREPARATION_STEPS = 2;

	public CreateListTask(AsyncCallback<IndexEntry[]> callback, ProgressMonitor<ShuffleProgress> progressMonitor) {
		super(callback, progressMonitor);
	}

	@Override
	protected IndexEntry[] doInBackground(NumberOfSongs... params) {
		try {
			return createNewShuffledList(params[0].context, params[0].value);
		} catch (Exception e) {
			callback.setException(e);
			return new IndexEntry[0];
		}
	}

	private IndexEntry[] createNewShuffledList(Context context, int numberOfSongs) throws Exception {
		publishProgress(PreparationStep.LOADING_INDEX);
		InputStream indexStream          = loadIndex(context);
		publishProgress(PreparationStep.SHUFFLING_INDEX);
		IndexEntry[]    shuffledIndexEntries = shuffleIndexEntries(indexStream, numberOfSongs);
		publishProgress(new DeterminedSongsStep(shuffledIndexEntries));
		copySongsToLocalDir(context, shuffledIndexEntries);
		return shuffledIndexEntries;
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
