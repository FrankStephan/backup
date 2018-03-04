package com.frozen_foo.shuffle_my_music_app.ui;

import android.app.Activity;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.Error;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressRunnable;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.ListCreationListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by Frank on 24.02.2018.
 */

public abstract class ShuffleProgressUpdate implements ShuffleProgressRunnable {

	private final Activity activity;
	private final ProgressBar progressBar;
	private final ListCreationListener listCreationListener;

	protected abstract void onError(Exception e);

	public ShuffleProgressUpdate(final Activity activity, final ProgressBar progressBar,
								 final ListCreationListener listCreationListener) {
		this.activity = activity;
		this.progressBar = progressBar;
		this.listCreationListener = listCreationListener;
	}

	@Override
	public void run(final ShuffleProgress shuffleProgress, final int numberOfSongs) {
		progressBar.setMax(4 + numberOfSongs);
		if (shuffleProgress instanceof PreparationStep) {
			PreparationStep preparationStep = (PreparationStep) shuffleProgress;
			switch (preparationStep) {
				case SAVING_FAVORITES:
					progressBar.setProgress(1);
					showPreparation(activity, activity.getString(R.string.saveFavorites));
					break;
				case LOADING_INDEX:
					progressBar.setProgress(2);
					showPreparation(activity, activity.getString(R.string.indexLoading));
					break;
				case SHUFFLING_INDEX:
					progressBar.setProgress(3);
					showPreparation(activity, activity.getString(R.string.determineRandomSongs));
					break;
				case DETERMINED_SONGS:
					progressBar.setProgress(4);
					try {
						fillRows(activity, new ShuffleAccess().getLocalIndex(activity));
					} catch (IOException e) {
						handleError(activity, e, listCreationListener);
					}
					break;
			}
		} else {
			if (shuffleProgress instanceof CopySongStep) {
				int index = ((CopySongStep) shuffleProgress).getIndex();
				updateCopyProgress(activity, index);
				progressBar.setProgress(5 + index);
			} else if (shuffleProgress instanceof FinalizationStep) {
				resetCopyProgressForAllSongs(activity);
				progressBar.setProgress(0);
				listCreationListener.onComplete();
			} else if (shuffleProgress instanceof Error) {
				handleError(activity, ((Error) shuffleProgress).getException(), listCreationListener);
			}
		}
	}

	private void resetCopyProgressForAllSongs(final Activity activity) {
		updateCopyProgress(activity, -1);
	}

	private void handleError(final Activity activity, final Exception e,
							 final ListCreationListener listCreationListener) {
		onError(e);
		listCreationListener.onComplete();
	}

	private void showPreparation(Activity activity, String text) {
		RowModel[]        rows    = new RowModel[]{new RowModel(text, null, false)};
		GenericRowAdapter adapter = new GenericRowAdapter(activity, rows);
		((ListView) activity.findViewById(R.id.shuffleList)).setAdapter(adapter);
	}

	private void fillRows(final Activity activity, List<IndexEntry> randomIndexEntries) {
		RowModel[]        rows    = new IndexEntryRowModelConverter().toRowModels(randomIndexEntries);
		GenericRowAdapter adapter = new GenericRowAdapter(activity, rows);
		((ListView) activity.findViewById(R.id.shuffleList)).setAdapter(adapter);
	}

	private void updateCopyProgress(Activity activity, int index) {
		GenericRowAdapter adapter =
				(GenericRowAdapter) ((ListView) activity.findViewById(R.id.shuffleList)).getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			RowModel rowModel = adapter.getItem(i);
			rowModel.setCopying(i == index);
		}
		adapter.notifyDataSetChanged();
	}
}
