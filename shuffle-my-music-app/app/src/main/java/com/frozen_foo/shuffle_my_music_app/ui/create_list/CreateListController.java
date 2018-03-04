package com.frozen_foo.shuffle_my_music_app.ui.create_list;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleListService;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressRunnable;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.ShuffleProgressUpdate;

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

	public BroadcastReceiver createShuffleProgressReceiver(final Activity activity, final ProgressBar progressBar,
														   final ListCreationListener listCreationListener) {
		return new ShuffleProgressReceiver(activity, progressBar, listCreationListener) {
			@Override
			protected ShuffleProgressRunnable createUIUpdate(final ShuffleProgress shuffleProgress, final int numberOfSongs,
															 final Activity activity, final ProgressBar progressBar,
															 final ListCreationListener listCreationListener) {
				return new ShuffleProgressUpdate(activity, progressBar, listCreationListener) {
					@Override
					protected void onError(Exception e) {
						alertException(activity, e);
					}
				};
			}
		};
	}

	public void registerShuffleProgressReceiver(Activity activity, BroadcastReceiver progressUpdater) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ShuffleListService.ACTION_CREATE_NEW_SHUFFLE_LIST);
		intentFilter.addAction(ShuffleListService.ACTION_RELOAD_SHUFFLE_LIST);
		LocalBroadcastManager.getInstance(activity).registerReceiver(progressUpdater, intentFilter);
	}

	public void unregisterShuffleProgressReceiver(Activity activity, BroadcastReceiver progressUpdater) {
		LocalBroadcastManager.getInstance(activity).unregisterReceiver(progressUpdater);
	}
}
