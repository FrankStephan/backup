package com.frozen_foo.shuffle_my_music_app.ui.create_list;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleListService;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressRunnable;

/**
 * Created by Frank on 25.02.2018.
 */

public abstract class ShuffleProgressReceiver extends BroadcastReceiver {

	private final Activity activity;
	private final ProgressBar progressBar;
	private final ListCreationListener listCreationListener;

	public ShuffleProgressReceiver(final Activity activity, final ProgressBar progressBar,
								   final ListCreationListener listCreationListener) {
		this.activity = activity;
		this.progressBar = progressBar;
		this.listCreationListener = listCreationListener;
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		final ShuffleProgress shuffleProgress = ShuffleListService.extractProgress(intent);
		final int             numberOfSongs   = ShuffleListService.extractNumberOfSongs(intent);
		runUIUpdate(context, shuffleProgress, numberOfSongs);

	}

	private void runUIUpdate(final Context context, final ShuffleProgress shuffleProgress, final int numberOfSongs) {
		ShuffleProgressRunnable uiUpdate =
				createUIUpdate(shuffleProgress, numberOfSongs, activity, progressBar, listCreationListener);
		Handler handler = new Handler(Looper.getMainLooper());
		new ShuffleProgressAccess(context).updateShuffleProgress(shuffleProgress, numberOfSongs, uiUpdate, handler);
	}

	protected abstract ShuffleProgressRunnable createUIUpdate(final ShuffleProgress shuffleProgress,
															  final int numberOfSongs, final Activity activity,
															  final ProgressBar progressBar,
															  final ListCreationListener listCreationListener);
}
