package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.Logger;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.Error;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Frank on 13.04.2018.
 */

public abstract class AbstractListProcess {

	private ProgressMonitor<ShuffleProgress> progressMonitor;

	public AbstractListProcess(ProgressMonitor<ShuffleProgress> progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	protected abstract List<IndexEntry> loadNewList(final Context context, InputStream indexStream, int numberOfSongs);

	public void start(NumberOfSongs numberOfSongs) {
		try {
			List<IndexEntry> shuffledList =
					executeProcessSteps(numberOfSongs.context, numberOfSongs.value, numberOfSongs.useExistingList);
		} catch (SettingsAccessException e) {
			Logger.logException(numberOfSongs.context, e);
			publishProgress(new Error(e));
		} catch (IOException e) {
			Logger.logException(numberOfSongs.context, e);
			publishProgress(new Error(e));
		}
	}

	private List<IndexEntry> executeProcessSteps(Context context, int numberOfSongs, boolean useExistingList) throws
			IOException, SettingsAccessException {
		List<IndexEntry> newList;
		if (useExistingList) {
			newList = new ShuffleAccess().getLocalIndex(context);
			publishProgress(PreparationStep.DETERMINED_SONGS);
			copySongsToLocalDir(context, newList);
		} else {
			publishProgress(PreparationStep.SAVING_FAVORITES);
			updateAndBackupFavorites(context);

			publishProgress(PreparationStep.LOADING_INDEX);
			InputStream indexStream = loadIndex(context);

			publishProgress(PreparationStep.SHUFFLING_INDEX);
			newList = loadNewList(context, indexStream, numberOfSongs);
			shuffleAccess().cleanLocalData(context);
			shuffleAccess().createLocalIndex(context, newList);

			publishProgress(PreparationStep.DETERMINED_SONGS);
			copySongsToLocalDir(context, newList);
		}
		publishProgress(new FinalizationStep());

		return newList;
	}

	private void publishProgress(final ShuffleProgress shuffleProgress) {
		if (progressMonitor != null) {
			progressMonitor.updateProgress(shuffleProgress);
		}
	}

	private void updateAndBackupFavorites(Context context) throws SettingsAccessException, IOException {
		shuffleAccess().addFavoritesToLocalCollection(context);
		shuffleAccess().backupFavoritesCollectionToRemote(context);
	}

	private InputStream loadIndex(Context context) throws SettingsAccessException, IOException {
		return shuffleAccess().loadIndexFromRemote(context);
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

	protected ShuffleAccess shuffleAccess() {
		return new ShuffleAccess();
	}
}
