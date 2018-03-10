package com.frozen_foo.shuffle_my_music_app.shuffle.progress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by Frank on 06.03.2018.
 */

public class ShuffleProgressReceiver extends BroadcastReceiver {

	protected final ShuffleProgressRunnable runnable;
	protected final Handler handler;
	protected final ShuffleProgressProcessor shuffleProgressProcessor;

	public ShuffleProgressReceiver(final ShuffleProgressRunnable runnable, final Handler handler,
								   final ShuffleProgressProcessor shuffleProgressProcessor) {
		this.runnable = runnable;
		this.handler = handler;
		this.shuffleProgressProcessor = shuffleProgressProcessor;
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		processProgressUpdate(context);
	}

	private void processProgressUpdate(final Context context) {
		shuffleProgressProcessor.processUpdateWithMostRecentProgressAsync(context, handler, runnable);
	}
}
