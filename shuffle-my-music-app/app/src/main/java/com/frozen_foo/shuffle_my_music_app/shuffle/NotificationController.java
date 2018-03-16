package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

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

	public Notification buildNotification(Context context, String contextText) {
		Intent        notificationIntent = new Intent(context, ShuffleListActivity.class);
		PendingIntent pendingIntent      = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		return new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_shuffle_white_24dp)
				.setContentTitle(context.getString(R.string.app_name)).setContentText(contextText)
				.setContentIntent(pendingIntent).build();
	}

	public void updateNotifications(Context context, final ShuffleProgress shuffleProgress, final NumberOfSongs numberOfSongs) {
		if (shuffleProgress instanceof PreparationStep) {
			PreparationStep preparationStep = (PreparationStep) shuffleProgress;
			switch (preparationStep) {
				case SAVING_FAVORITES:
					updateNotifications(context, context.getString(R.string.saveFavorites));
					break;
				case LOADING_INDEX:
					updateNotifications(context, context.getString(R.string.indexLoading));
					break;
				case SHUFFLING_INDEX:
					updateNotifications(context, context.getString(R.string.determineRandomSongs));
					break;
			}
		} else {
			if (shuffleProgress instanceof CopySongStep) {
				String notificationMessage = new StringBuilder().append(context.getString(R.string.copying_title)).append(" ")
						.append(((CopySongStep) shuffleProgress).getIndex() + 1).append("/")
						.append(numberOfSongs.value).toString();
				updateNotifications(context, notificationMessage);
			} else if (shuffleProgress instanceof FinalizationStep) {
				notificationManager(context).cancel(NOTIFICATION_ID);
			}
		}
	}

	private void updateNotifications(Context context, String contextText) {
		notificationManager(context).notify(NOTIFICATION_ID, buildNotification(context, contextText));
	}

	private void updateNotifications(Context context,Notification notification) {
		notificationManager(context).notify(NOTIFICATION_ID, notification);
	}

	private NotificationManager notificationManager(Context context) {
		return context.getSystemService(NotificationManager.class);
	}
}
