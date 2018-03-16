package com.frozen_foo.shuffle_my_music_app.ui;

import android.app.Activity;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.durations.DurationsAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressRunnable;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.Error;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.ListCreationListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank on 24.02.2018.
 */

public abstract class ShuffleProgressUpdate implements ShuffleProgressRunnable {

	private final Activity activity;
	private final ProgressBar progressBar;
	private final ListCreationListener listCreationListener;
	private final boolean forceReloadRows;

	public ShuffleProgressUpdate(final Activity activity, final ProgressBar progressBar,
								 final ListCreationListener listCreationListener, final boolean forceFillRows) {
		this.activity = activity;
		this.progressBar = progressBar;
		this.listCreationListener = listCreationListener;
		this.forceReloadRows = forceFillRows;
	}

	protected abstract void onError(Exception e);

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
					fillRows();
					break;
			}
		} else {
			if (forceReloadRows) {
				fillRows();
			}
			if (shuffleProgress instanceof CopySongStep) {
				int index = ((CopySongStep) shuffleProgress).getIndex();
				updateRows(index);
				progressBar.setProgress(5 + index);
			} else if (shuffleProgress instanceof FinalizationStep) {
				GenericRowAdapter adapter = listAdapter();
				resetCopyProgressForAllSongs(adapter);
				updateDurations(numberOfSongs - 1, adapter);
				progressBar.setProgress(0);
				listCreationListener.onComplete();
			} else if (shuffleProgress instanceof Error) {
				handleError(activity, ((Error) shuffleProgress).getException(), listCreationListener);
			}
		}
	}

	private GenericRowAdapter listAdapter() {
		return (GenericRowAdapter) ((ListView) activity.findViewById(R.id.shuffleList)).getAdapter();
	}

	private void resetCopyProgressForAllSongs(final GenericRowAdapter adapter) {
		updateCopyProgress(-1, adapter);
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

	private void fillRows() {
		List<IndexEntry>  indexEntries = new ShuffleAccess().getLocalIndex(activity);
		RowModel[]        rows         = new IndexEntryRowModelConverter().toRowModels(indexEntries);
		GenericRowAdapter adapter      = new GenericRowAdapter(activity, rows);
		((ListView) activity.findViewById(R.id.shuffleList)).setAdapter(adapter);
	}

	private void updateRows(int index) {
		GenericRowAdapter adapter = listAdapter();
		updateCopyProgress(index, adapter);
		updateDurations(index - 1, adapter);
		adapter.notifyDataSetChanged();
	}

	private void updateCopyProgress(int index, GenericRowAdapter adapter) {
		for (int i = 0; i < adapter.getCount(); i++) {
			RowModel rowModel = adapter.getItem(i);
			rowModel.setCopying(i == index);
		}
	}

	private void updateDurations(int index, GenericRowAdapter adapter) {
		if (index >= 0) {
			if (forceReloadRows) {
				updateAllDurations(index, adapter);
			} else {
				updateSingleDuration(index, adapter);
			}
		}
	}

	private void updateSingleDuration(final int index, final GenericRowAdapter adapter) {
		int duration = new DurationsAccess(activity).duration(index, 0);
		setDurationForUI(index, adapter, duration);
	}

	private void updateAllDurations(final int index, final GenericRowAdapter adapter) {
		for (int i = 0; i <= index; i++) {
			updateSingleDuration(i, adapter);
		}
	}

	private void setDurationForUI(final int i, final GenericRowAdapter adapter, final int duration) {
		if (duration >= 0) {
			adapter.getItem(i).setDuration(timeFormat().format(new Date(duration)));
		} else {
			adapter.getItem(i).setDuration(activity.getString(R.string.file_corrupted));
		}
	}

	private SimpleDateFormat timeFormat() {
		return new SimpleDateFormat("mm:ss");
	}
}
