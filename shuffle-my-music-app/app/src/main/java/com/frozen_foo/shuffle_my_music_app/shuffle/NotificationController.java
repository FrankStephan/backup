package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.v4.app.NotificationCompat;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.ShuffleListActivity;

/**
 * Created by Frank on 08.03.2018.
 */

public class NotificationController {

	public static final int NOTIFICATION_ID = 1;

	public Notification buildNotification(Context context, String contextText, int progress, int max) {
		Intent        notificationIntent = new Intent(context, ShuffleListActivity.class);
		PendingIntent pendingIntent      = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		return new Notification.Builder(context)
				.setSmallIcon( R.drawable.ic_shuffle_white_24dp)
				.setContentText(contextText)
				.setContentTitle(context.getString(R.string.app_name))
				.setContentIntent(pendingIntent)
				.setOngoing(true)
				.setProgress(max, progress, false)
				.build();
	}

	public void updateNotifications(Context context, final ShuffleProgress shuffleProgress, final NumberOfSongs numberOfSongs) {
		int progressMax = numberOfSongs.value + 4;
		if (shuffleProgress instanceof PreparationStep) {
			PreparationStep preparationStep = (PreparationStep) shuffleProgress;
			switch (preparationStep) {
				case SAVING_FAVORITES:
					updateNotifications(context, context.getString(R.string.saveFavorites), 1, progressMax);
					break;
				case LOADING_INDEX:
					updateNotifications(context, context.getString(R.string.indexLoading), 2, progressMax);
					break;
				case SHUFFLING_INDEX:
					updateNotifications(context, context.getString(R.string.determineRandomSongs), 3, progressMax);
					break;
			}
		} else {
			if (shuffleProgress instanceof CopySongStep) {
				int index = ((CopySongStep) shuffleProgress).getIndex();
				String notificationMessage = new StringBuilder().append(context.getString(R.string.copying_title)).append(" ")
						.append(index + 1).append("/")
						.append(numberOfSongs.value).toString();
				updateNotifications(context, notificationMessage, 4 + index, progressMax);
			} else if (shuffleProgress instanceof FinalizationStep) {
				notificationManager(context).cancel(NOTIFICATION_ID);
			}
		}
	}

	private void updateNotifications(Context context, String contextText, int progress, int max) {
		notificationManager(context).notify(NOTIFICATION_ID, buildNotification(context, contextText, progress, max));
	}

	private void updateNotifications(Context context,Notification notification) {
		notificationManager(context).notify(NOTIFICATION_ID, notification);
	}

	private NotificationManager notificationManager(Context context) {
		return context.getSystemService(NotificationManager.class);
	}
}
