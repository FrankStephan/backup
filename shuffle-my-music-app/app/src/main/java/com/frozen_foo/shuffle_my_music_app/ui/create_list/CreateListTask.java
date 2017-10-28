package com.frozen_foo.shuffle_my_music_app.ui.create_list;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.async.AbstractAsyncTask;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.DeterminedSongsStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.StartSongCopyStep;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

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
		saveAndBackupFavorites(context);

		publishProgress(PreparationStep.LOADING_INDEX);
		InputStream indexStream = loadIndex(context);

		publishProgress(PreparationStep.SHUFFLING_INDEX);
		List<IndexEntry> shuffledIndexEntries = shuffleIndexEntries(indexStream, numberOfSongs);

		publishProgress(new DeterminedSongsStep(shuffledIndexEntries));
		copySongsToLocalDir(context, shuffledIndexEntries);

		return shuffledIndexEntries;
	}

	private void saveAndBackupFavorites(final Context context) throws IOException, CertificateException,
			NoSuchAlgorithmException, InvalidKeyException, UnrecoverableEntryException,
			InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchProviderException, KeyStoreException {
		shuffleAccess().addFavoritesToLocalCollection();
		shuffleAccess().backupFavoritesCollectionToRemote(context);
	}

	private InputStream loadIndex(Context context) throws Exception {
		return shuffleAccess().loadIndexFromRemote(context);
	}

	private List<IndexEntry> shuffleIndexEntries(InputStream indexStream, int numberOfSongs) {
		return shuffleAccess().shuffleIndexEntries(indexStream, numberOfSongs);
	}

	private void copySongsToLocalDir(Context context, List<IndexEntry> shuffledIndexEntries) throws Exception {
		shuffleAccess().cleanLocalData();
		shuffleAccess().createLocalIndex(shuffledIndexEntries);

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
