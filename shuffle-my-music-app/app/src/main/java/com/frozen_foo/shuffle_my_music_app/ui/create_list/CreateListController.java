package com.frozen_foo.shuffle_my_music_app.ui.create_list;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleListService;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.GenericRowAdapter;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.Error;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.StartSongCopyStep;

import java.io.IOException;
import java.util.List;

/**
 * Created by Frank on 05.08.2017.
 */

public class CreateListController extends AbstractListController {

	public void createShuffleList(final Activity activity, ProgressBar progressBar, int numberOfSongs,
								  boolean useExistingList) {
		progressBar.setProgress(0);
		NumberOfSongs createListParams = new NumberOfSongs(numberOfSongs, activity, useExistingList);
		if (useExistingList) {
			ShuffleListService.reloadShuffleList(activity, numberOfSongs);
		} else {
			ShuffleListService.createNewShuffleList(activity, numberOfSongs);
		}
	}

	public BroadcastReceiver createProgressUpdater(final Activity activity, final ProgressBar progressBar,
												   final ListCreationListener listCreationListener) {
		return new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				final ShuffleProgress shuffleProgress = ShuffleListService.extractProgress(intent);
				Handler               handler         = new Handler(Looper.getMainLooper());
				handler.post(new Runnable() {
					@Override
					public void run() {
						new CreateListController()
								.updateProgress(activity, progressBar, shuffleProgress, intent, listCreationListener);
					}
				});
			}
		};
	}

	private void updateProgress(Activity activity, ProgressBar progressBar, ShuffleProgress shuffleProgress,
								final Intent intent, final ListCreationListener listCreationListener) {
		int numberOfSongs = ShuffleListService.extractNumberOfSongs(intent);
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
			if (shuffleProgress instanceof StartSongCopyStep) {
				int index = ((StartSongCopyStep) shuffleProgress).getIndex();
				updateCopyProgress(activity, index);
				progressBar.setProgress(5 + index);
			} else if (shuffleProgress instanceof FinishedSongCopyStep) {
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
		alertException(activity, e);
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

	public void registerProgressUpdater(Activity activity, BroadcastReceiver progressUpdater) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ShuffleListService.ACTION_CREATE_NEW_SHUFFLE_LIST);
		intentFilter.addAction(ShuffleListService.ACTION_RELOAD_SHUFFLE_LIST);
		LocalBroadcastManager.getInstance(activity).registerReceiver(progressUpdater, intentFilter);
	}

	public void unregisterProgressUpdater(Activity activity, BroadcastReceiver progressUpdater) {
		LocalBroadcastManager.getInstance(activity).unregisterReceiver(progressUpdater);
	}
}
