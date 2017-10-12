package com.frozen_foo.shuffle_my_music_app.main.create_list;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.io.remote.RemoteDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.DeterminedSongsStep;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.main.create_list.progress.StartSongCopyStep;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by Frank on 01.08.2017.
 */

public class CreateListTask extends AbstractAsyncTask<NumberOfSongs, ShuffleProgress, List<IndexEntry>> {

	public CreateListTask(AsyncCallback<List<IndexEntry>> callback, ProgressMonitor<ShuffleProgress> progressMonitor) {
		super(callback, progressMonitor);
	}

	@Override
	protected List<IndexEntry> doInBackground(NumberOfSongs... params) {
		try {
			return createNewShuffledList(params[0].context, params[0].value);
		} catch (Exception e) {
			callback.setException(e);
			return Collections.emptyList();
		}
	}

	private List<IndexEntry> createNewShuffledList(Context context, int numberOfSongs) throws Exception {
		publishProgress(PreparationStep.SAVING_FAVORITES);
		joinAndSaveFavoritesToRemote(context);
		publishProgress(PreparationStep.LOADING_INDEX);
		InputStream indexStream = loadIndex(context);
		publishProgress(PreparationStep.SHUFFLING_INDEX);
		List<IndexEntry> shuffledIndexEntries = shuffleIndexEntries(indexStream, numberOfSongs);
		publishProgress(new DeterminedSongsStep(shuffledIndexEntries));
		copySongsToLocalDir(context, shuffledIndexEntries);
		return shuffledIndexEntries;
	}

	private void joinAndSaveFavoritesToRemote(Context context) {
		String localDirPath = new LocalDirectoryAccess().localDir().getPath();
		List<IndexEntry> favorites =
				new ShuffleMyMusicService().loadFavorites(localDirPath);
		List<IndexEntry> newFavorites =
				new ShuffleMyMusicService().loadFavorites(new LocalDirectoryAccess().localSongsDir().getPath());
		List<IndexEntry> resultingFavorites = new ShuffleMyMusicService().join(newFavorites, favorites);
		new ShuffleMyMusicService().addFavorites(localDirPath, resultingFavorites);
	}

	private InputStream loadIndex(Context context) throws Exception {
		return new RemoteDirectoryAccess().indexStream(context);
	}

	private List<IndexEntry> shuffleIndexEntries(InputStream indexStream, int numberOfSongs) {
		return new ShuffleMyMusicService().randomIndexEntries(indexStream, numberOfSongs);
	}

	private void copySongsToLocalDir(Context context, List<IndexEntry> shuffledIndexEntries) throws Exception {
		RemoteDirectoryAccess remoteDirectoryAccess = new RemoteDirectoryAccess();
		LocalDirectoryAccess  localDirectoryAccess  = new LocalDirectoryAccess();
		localDirectoryAccess.cleanLocalDir();

		createLocalIndex(shuffledIndexEntries);

		for (int i = 0; i < shuffledIndexEntries.size(); i++) {
			publishProgress(new StartSongCopyStep(i));
			InputStream remoteSongStream = remoteDirectoryAccess.songStream(context, shuffledIndexEntries.get(i).getPath());
			localDirectoryAccess.copyToLocal(remoteSongStream, shuffledIndexEntries.get(i).getFileName());
			publishProgress(new FinishedSongCopyStep(i));
		}
		publishProgress(new FinalizationStep());
	}

	private void createLocalIndex(List<IndexEntry> shuffledIndexEntries) {
		LocalDirectoryAccess localDirectoryAccess = new LocalDirectoryAccess();
		new ShuffleMyMusicService().createSongsFile(localDirectoryAccess.localSongsDir().getPath(), shuffledIndexEntries);
	}
}
