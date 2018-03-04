package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.Logger;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.NumberOfSongs;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.Error;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.CopySongStep;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Frank on 01.08.2017.
 */

public class ShuffleListProcess {

	ProgressMonitor<ShuffleProgress> progressMonitor;

	public ShuffleListProcess(ProgressMonitor<ShuffleProgress> progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	public void start(NumberOfSongs numberOfSongs) {
		try {
			List<IndexEntry> shuffledList =
					createNewShuffledList(numberOfSongs.context, numberOfSongs.value, numberOfSongs.useExistingList);
		} catch (SettingsAccessException e) {
			Logger.logException(numberOfSongs.context, e);
			publishProgress(new Error(e));
		} catch (IOException e) {
			Logger.logException(numberOfSongs.context, e);
			publishProgress(new Error(e));
		}
	}

	private List<IndexEntry> createNewShuffledList(Context context, int numberOfSongs, boolean useExistingList) throws
			IOException, SettingsAccessException {
		List<IndexEntry> shuffledIndexEntries;
		if (useExistingList) {
			shuffledIndexEntries = new ShuffleAccess().getLocalIndex(context);
			publishProgress(PreparationStep.DETERMINED_SONGS);
			copySongsToLocalDir(context, shuffledIndexEntries);
		} else {
			publishProgress(PreparationStep.SAVING_FAVORITES);
			saveAndBackupFavorites(context);

			publishProgress(PreparationStep.LOADING_INDEX);
			InputStream indexStream = loadIndex(context);

			publishProgress(PreparationStep.SHUFFLING_INDEX);
			shuffledIndexEntries = shuffleIndexEntries(indexStream, numberOfSongs);
			shuffleAccess().cleanLocalData(context);
			shuffleAccess().createLocalIndex(context, shuffledIndexEntries);

			publishProgress(PreparationStep.DETERMINED_SONGS);
			copySongsToLocalDir(context, shuffledIndexEntries);
		}
		publishProgress(new FinalizationStep());

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
		for (int i = 0; i < shuffledIndexEntries.size(); i++) {
			publishProgress(new CopySongStep(i));
			copyToLocalWithRetry(context, shuffledIndexEntries.get(i));
		}
	}

	private void copyToLocalWithRetry(Context context, IndexEntry indexEntry) throws SettingsAccessException, IOException {
		int retryAttempt = 0;
		boolean success  = false;

		while(!success && retryAttempt <= 2) {
			try {
				shuffleAccess().copySongFromRemoteToLocal(context, indexEntry);
				success = true;
			} catch (IOException ioe) {
				if (2 == retryAttempt) {
					throw ioe;
				} else {
					retryAttempt++;
					Logger.logException(context, ioe);
					Logger.log(context, "Retry attempt number " + retryAttempt + " for " + indexEntry.getFileName());
					try {
						Thread.sleep(10000L);
					} catch (InterruptedException ie) {
						Logger.logException(context, ie);
					}
				}
			}
		}
	}

	private ShuffleAccess shuffleAccess() {
		return new ShuffleAccess();
	}
}
