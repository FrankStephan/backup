package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.Logger;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.NumberOfSongs;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.DeterminedSongsStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.StartSongCopyStep;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Frank on 01.08.2017.
 */

public class ShuffleListProcess {

	AsyncCallback<List<IndexEntry>> callback;
	ProgressMonitor<ShuffleProgress> progressMonitor;

	public ShuffleListProcess(AsyncCallback<List<IndexEntry>> callback, ProgressMonitor<ShuffleProgress> progressMonitor) {
		this.callback = callback;
		this.progressMonitor = progressMonitor;
	}

	public void start(NumberOfSongs numberOfSongs) {
		try {
			List<IndexEntry> shuffledList =
					createNewShuffledList(numberOfSongs.context, numberOfSongs.value, numberOfSongs.useExistingList);
			callback.invoke(shuffledList);
		} catch (SettingsAccessException e) {
			Logger.logException(numberOfSongs.context, e);
			callback.setException(e);
		} catch (IOException e) {
			Logger.logException(numberOfSongs.context, e);
			callback.setException(e);
		}
	}

	private List<IndexEntry> createNewShuffledList(Context context, int numberOfSongs, boolean useExistingList) throws
			IOException, SettingsAccessException {
		List<IndexEntry> shuffledIndexEntries;
		if (useExistingList) {
			publishProgress(PreparationStep.SAVING_FAVORITES);
			publishProgress(PreparationStep.LOADING_INDEX);
			publishProgress(PreparationStep.SHUFFLING_INDEX);
			shuffledIndexEntries = new ShuffleAccess().getLocalIndex(context);
			publishProgress(new DeterminedSongsStep(shuffledIndexEntries));
			copySongsToLocalDir(context, shuffledIndexEntries);
		} else {
			publishProgress(PreparationStep.SAVING_FAVORITES);
			saveAndBackupFavorites(context);

			publishProgress(PreparationStep.LOADING_INDEX);
			InputStream indexStream = loadIndex(context);

			publishProgress(PreparationStep.SHUFFLING_INDEX);
			shuffledIndexEntries = shuffleIndexEntries(indexStream, numberOfSongs);

			publishProgress(new DeterminedSongsStep(shuffledIndexEntries));
			copySongsToLocalDir(context, shuffledIndexEntries);
		}

		return shuffledIndexEntries;
	}

	private void publishProgress(final ShuffleProgress shuffleProgress) {
		if (progressMonitor != null) {
			progressMonitor.updateProgress(shuffleProgress);
		}
	}

	private void saveAndBackupFavorites(final Context context) throws SettingsAccessException, IOException {
		shuffleAccess().addFavoritesToLocalCollection(context);
		shuffleAccess().backupFavoritesCollectionToRemote(context);
	}

	private InputStream loadIndex(Context context) throws SettingsAccessException, IOException {
		return shuffleAccess().loadIndexFromRemote(context);
	}

	private List<IndexEntry> shuffleIndexEntries(InputStream indexStream, int numberOfSongs) {
		return shuffleAccess().shuffleIndexEntries(indexStream, numberOfSongs);
	}

	private void copySongsToLocalDir(Context context, List<IndexEntry> shuffledIndexEntries) throws IOException,
			SettingsAccessException {
		shuffleAccess().cleanLocalData(context);
		shuffleAccess().createLocalIndex(context, shuffledIndexEntries);

		for (int i = 0; i < shuffledIndexEntries.size(); i++) {
			publishProgress(new StartSongCopyStep(i));
			shuffleAccess().copySongFromRemoteToLocal(context, shuffledIndexEntries.get(i));
			publishProgress(new FinishedSongCopyStep(i));
		}
		publishProgress(new FinalizationStep());
	}

	private ShuffleAccess shuffleAccess() {
		return new ShuffleAccess();
	}
}
