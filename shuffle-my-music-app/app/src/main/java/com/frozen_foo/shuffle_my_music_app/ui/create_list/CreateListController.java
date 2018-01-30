package com.frozen_foo.shuffle_my_music_app.ui.create_list;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.GenericRowAdapter;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.DeterminedSongsStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.StartSongCopyStep;

import java.util.List;

/**
 * Created by Frank on 05.08.2017.
 */

public class CreateListController extends AbstractListController {

	public void createShuffleList(final Activity activity, ProgressBar progressBar, int numberOfSongs,
								  boolean useExistingList, ListCreationListener listCreationListener) {
		progressBar.setMax(numberOfSongs + PreparationStep.values().length);
		progressBar.setProgress(0);
		NumberOfSongs createListParams =
				new NumberOfSongs(numberOfSongs, activity, useExistingList);
		new CreateListTask(inflateListCallback(activity, listCreationListener),
				progressBarUpdater(activity, progressBar)).execute(createListParams);
	}

	@NonNull
	private ProgressMonitor<ShuffleProgress> progressBarUpdater(final Activity activity, final ProgressBar progressBar) {
		return new ProgressMonitor<ShuffleProgress>() {
			@Override
			public void updateProgress(final ShuffleProgress shuffleProgress) {
				setProgress(activity, progressBar, shuffleProgress);
			}
		};
	}

	@NonNull
	private AsyncCallback<List<IndexEntry>> inflateListCallback(final Activity activity,
																final ListCreationListener listCreationListener) {
		return new AsyncCallback<List<IndexEntry>>() {
			@Override
			public void invoke(List<IndexEntry> result) {
				if (hasException()) {
					alertException(activity, getException());
				} else {
					listCreationListener.onComplete();
				}
			}
		};
	}

	private void setProgress(Activity activity, ProgressBar progressBar, ShuffleProgress shuffleProgress) {
		if (shuffleProgress instanceof PreparationStep) {
			PreparationStep preparationStep = (PreparationStep) shuffleProgress;
			switch (preparationStep) {
				case SAVING_FAVORITES:
					showPreparation(activity, activity.getString(R.string.saveFavorites));
					progressBar.setProgress(0);
					break;
				case LOADING_INDEX:
					showPreparation(activity, activity.getString(R.string.indexLoading));
					progressBar.setProgress(1);
					break;
				case SHUFFLING_INDEX:
					showPreparation(activity, activity.getString(R.string.determineRandomSongs));
					progressBar.incrementProgressBy(2);
					break;
			}
		} else {
			if (shuffleProgress instanceof DeterminedSongsStep) {
				fillRows(activity, ((DeterminedSongsStep) shuffleProgress).getSongs());
				progressBar.incrementProgressBy(1);
			} else if (shuffleProgress instanceof StartSongCopyStep) {
				updateCopyProgress(activity, ((StartSongCopyStep) shuffleProgress).getIndex(), true);
			} else if (shuffleProgress instanceof FinishedSongCopyStep) {
				updateCopyProgress(activity, ((FinishedSongCopyStep) shuffleProgress).getIndex(), false);
				progressBar.incrementProgressBy(1);
			} else if (shuffleProgress instanceof FinalizationStep) {
				progressBar.setProgress(0);
			}
		}
	}

	private void showPreparation(Activity activity, String text) {
		RowModel[]         rows    = new RowModel[]{new RowModel(text, null, false)};
		GenericRowAdapter adapter = new GenericRowAdapter(activity, rows);
		((ListView) activity.findViewById(R.id.shuffleList)).setAdapter(adapter);
	}

	private void fillRows(final Activity activity, List<IndexEntry> randomIndexEntries) {
		RowModel[]        rows    = new IndexEntryRowModelConverter().toRowModels(randomIndexEntries);
		GenericRowAdapter adapter = new GenericRowAdapter(activity, rows);
		((ListView) activity.findViewById(R.id.shuffleList)).setAdapter(adapter);
	}

	private void updateCopyProgress(Activity activity, int index, boolean copying) {
		GenericRowAdapter adapter =
				(GenericRowAdapter) ((ListView) activity.findViewById(R.id.shuffleList)).getAdapter();
		RowModel rowModel = adapter.getItem(index);
		rowModel.setCopying(copying);
		adapter.notifyDataSetChanged();
	}
}
