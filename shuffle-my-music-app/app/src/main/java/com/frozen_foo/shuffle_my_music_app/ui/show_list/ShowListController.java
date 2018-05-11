package com.frozen_foo.shuffle_my_music_app.ui.show_list;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressProcessor;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.GenericRowAdapter;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;
import com.frozen_foo.shuffle_my_music_app.ui.ShuffleProgressUpdate;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.ListCreationListener;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Frank on 04.08.2017.
 */

public class ShowListController extends AbstractListController {

	@NonNull
	private SimpleDateFormat timeFormat() {
		return new SimpleDateFormat("mm:ss");
	}

	public void loadAndInflateList(final Activity activity, ListView shuffleList, ProgressBar progressBar,
								   ListCreationListener listCreationListener) {
		Context          context      = activity.getApplicationContext();
		List<IndexEntry> indexEntries = localIndex(activity);
		RowModel[]       rows         = new IndexEntryRowModelConverter().toRowModels(indexEntries);

		GenericRowAdapter adapter = new GenericRowAdapter(activity);
		adapter.addAll(rows);
		shuffleList.setAdapter(adapter);
		adapter.setShowDurations(true);

		updateProgressIfShuffleIsRunning(activity, shuffleList, progressBar, listCreationListener, context);
		adapter.notifyDataSetChanged();
	}

	private void updateProgressIfShuffleIsRunning(final Activity activity, ListView shuffleList, final ProgressBar progressBar,
												  final ListCreationListener listCreationListener,
												  final Context context) {
		new ShuffleProgressProcessor().processUpdateWithMostRecentProgressSync(context,
				new ShuffleProgressUpdate(activity, shuffleList, progressBar, listCreationListener, true) {
					@Override
					protected void onError(final Exception e) {
						alertException(activity, e);
					}
				});
	}

	public void markAsPlayingSong(Activity activity, ListView shuffleList, int index) {
		GenericRowAdapter adapter = (GenericRowAdapter) shuffleList.getAdapter();
		if (adapter != null) {
			for (int i = 0; i < adapter.getCount(); i++) {
				adapter.getItem(i).setPlaying(i == index);
			}

			adapter.notifyDataSetChanged();
		}
	}
}
