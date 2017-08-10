package com.frozen_foo.shuffle_my_music_app.list.create;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.Util;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.list.NumberOfSongs;
import com.frozen_foo.shuffle_my_music_app.list.RowAdapter;
import com.frozen_foo.shuffle_my_music_app.list.RowModel;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.DeterminedSongsStep;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.list.create.progress.StartSongCopyStep;

import java.io.File;

/**
 * Created by Frank on 05.08.2017.
 */

public class CreateShuffleListController {

	public void createShuffleList(Context context, final Activity activity, ProgressBar progressBar, int numberOfSongs) {
		progressBar.setMax(numberOfSongs + PreparationStep.values().length);
		NumberOfSongs numberOfSongsContext = new NumberOfSongs(numberOfSongs, context);
		new Shuffler(inflateListCallback(context, activity), progressBarUpdater(activity, progressBar)).execute(numberOfSongsContext);
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
	private AsyncCallback<File[]> inflateListCallback(final Context context, final Activity activity) {
		return new AsyncCallback<File[]>() {
			@Override
			public void invoke(File[] result) {
				if (hasException()) {
					Toast.makeText(context, getException().getMessage(), Toast.LENGTH_LONG).show();
				} else {
					fillRows(activity, Util.toFileNameList(result));
				}
			}
		};
	}

	private void setProgress(Activity activity, ProgressBar progressBar, ShuffleProgress shuffleProgress) {
		if (shuffleProgress instanceof PreparationStep) {
			PreparationStep preparationStep = (PreparationStep) shuffleProgress;
			switch (preparationStep) {
				case LOADING_INDEX:
					showPreparation(activity, activity.getString(R.string.indexLoading));
					progressBar.setProgress(0);
					break;
				case SHUFFLING_INDEX:
					showPreparation(activity, activity.getString(R.string.determineRandomSongs));
					progressBar.incrementProgressBy(1);
					break;
			}
		} else {
			if (shuffleProgress instanceof DeterminedSongsStep) {
				fillRows(activity, ((DeterminedSongsStep)shuffleProgress).getSongs());
				progressBar.incrementProgressBy(1);
			} else if (shuffleProgress instanceof StartSongCopyStep){
				updateCopyProgress(activity, ((StartSongCopyStep)shuffleProgress).getIndex(), true);
			} else if (shuffleProgress instanceof FinishedSongCopyStep) {
				updateCopyProgress(activity, ((FinishedSongCopyStep)shuffleProgress).getIndex(), false);
				progressBar.incrementProgressBy(1);
			}
		}
	}

	private void showPreparation(Activity activity, String text) {
		RowModel[] rows    = new RowModel[]{new RowModel(text, null, false)};
		RowAdapter adapter = new RowAdapter(activity, rows);
		((ListView) activity.findViewById(R.id.shuffleList)).setAdapter(adapter);
	}

	private void fillRows(final Activity activity, String[] randomIndexEntries) {
		RowModel[] rows = new RowModel[randomIndexEntries.length];
		for (int i = 0; i < randomIndexEntries.length; i++) {
			rows[i] = new RowModel(randomIndexEntries[i], randomIndexEntries[i], false);
		}

		ProgressRowAdapter adapter = new ProgressRowAdapter(activity, rows);
		((ListView) activity.findViewById(R.id.shuffleList)).setAdapter(adapter);
	}

	private void updateCopyProgress(Activity activity, int index, boolean copying) {
		ProgressRowAdapter adapter  =
				(ProgressRowAdapter) ((ListView) activity.findViewById(R.id.shuffleList)).getAdapter();
		RowModel    rowModel = adapter.getItem(index);
		rowModel.setCopying(copying);
		adapter.notifyDataSetChanged();
	}
}
