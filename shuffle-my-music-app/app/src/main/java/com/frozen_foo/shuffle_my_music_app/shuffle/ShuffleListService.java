package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.ui.ShuffleListActivity;

public class ShuffleListService extends IntentService {

	private static final String ACTION_CREATE_NEW_SHUFFLE_LIST = "com.frozen_foo.myapplication.action.CREATE_NEW_SHUFFLE_LIST";
	private static final String ACTION_RELOAD_SHUFFLE_LIST = "com.frozen_foo.myapplication.action.RELOAD_SHUFFLE_LIST";

	private static final String NUMBER_OF_SONGS = "com.frozen_foo.myapplication.extra.NUMBER_OF_SONGS";
	public static final int DEFAULT_NUMBER_OF_SONGS = 10;
	public static final int NOTIFICATION_ID = 1;

	public ShuffleListService() {
		super("ShuffleListService");
	}

	public static void createNewShuffleList(Context context, int numberOfSongs) {
		Intent intent = new Intent(context, ShuffleListService.class);
		intent.setAction(ACTION_CREATE_NEW_SHUFFLE_LIST);
		intent.putExtra(NUMBER_OF_SONGS, numberOfSongs);
		context.startService(intent);
	}

	public static void reloadShuffleList(Context context) {
		Intent intent = new Intent(context, ShuffleListService.class);
		intent.setAction(ACTION_RELOAD_SHUFFLE_LIST);
		context.startService(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Intent notificationIntent = new Intent(this, ShuffleListActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		Notification notification = new NotificationCompat.Builder(this)
				.setContentTitle("My Awesome App")
				.setContentText("Doing some work...")
				.setContentIntent(pendingIntent).build();

		startForeground(NOTIFICATION_ID, notification);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_CREATE_NEW_SHUFFLE_LIST.equals(action)) {
				int numberOfSongs = intent.getIntExtra(NUMBER_OF_SONGS, DEFAULT_NUMBER_OF_SONGS);
				createNewShuffleList(numberOfSongs);
			} else if (ACTION_RELOAD_SHUFFLE_LIST.equals(action)) {
				reloadShuffleList();
			}

			// stopForeground(true);
			stopSelf();
		}
	}

	/**
	 * Handle action Foo in the provided background thread with the provided
	 * parameters.
	 */
	private void createNewShuffleList(int numberOfSongs) {
		// TODO: Handle action Foo
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Handle action Baz in the provided background thread with the provided
	 * parameters.
	 */
	private void reloadShuffleList() {
		// TODO: Handle action Baz
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
