package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.durations.DurationsAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.ShuffleListActivity;

public class ShuffleListService extends IntentService {

	public static final String ACTION_CREATE_NEW_SHUFFLE_LIST =
			"com.frozen_foo.myapplication.action.CREATE_NEW_SHUFFLE_LIST";
	public static final String ACTION_RELOAD_SHUFFLE_LIST = "com.frozen_foo.myapplication.action.RELOAD_SHUFFLE_LIST";

	public static final int DEFAULT_NUMBER_OF_SONGS = 0;

	private static final String NUMBER_OF_SONGS = "com.frozen_foo.myapplication.extra.NUMBER_OF_SONGS";
	private LocalBroadcastManager broadcaster;


	public ShuffleListService() {
		super("ShuffleListService");
	}

	public static void createNewShuffleList(Context context, int numberOfSongs) {
		startService(context, numberOfSongs, ACTION_CREATE_NEW_SHUFFLE_LIST);
	}

	public static void reloadShuffleList(Context context, int numberOfSongs) {
		startService(context, numberOfSongs, ACTION_RELOAD_SHUFFLE_LIST);
	}

	private static void startService(final Context context, final int numberOfSongs, final String action) {
		Intent intent = new Intent(context, ShuffleListService.class);
		intent.setAction(action);
		putNumberOfSongs(numberOfSongs, intent);
		context.startService(intent);
	}

	private static int extractNumberOfSongs(final Intent intent) {
		return intent.getIntExtra(NUMBER_OF_SONGS, DEFAULT_NUMBER_OF_SONGS);
	}

	private static void putNumberOfSongs(int numberOfSongs, Intent intent) {
		intent.putExtra(NUMBER_OF_SONGS, numberOfSongs);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		broadcaster = LocalBroadcastManager.getInstance(this);

		Intent notificationIntent = new Intent(this, ShuffleListActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification notification = new NotificationController().buildNotification(this, "");
		startForeground(NotificationController.NOTIFICATION_ID, notification);
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action        = intent.getAction();
			int          numberOfSongs = intent.getIntExtra(NUMBER_OF_SONGS, DEFAULT_NUMBER_OF_SONGS);
			if (ACTION_CREATE_NEW_SHUFFLE_LIST.equals(action)) {
				createNewShuffleList(numberOfSongs, intent);
			} else if (ACTION_RELOAD_SHUFFLE_LIST.equals(action)) {
				reloadShuffleList(numberOfSongs, intent);
			}
			stopForeground(true);
			stopSelf();
		}
	}

	private void createNewShuffleList(final int numberOfSongs, final Intent intent) {
		startShuffleListProcess(new NumberOfSongs(numberOfSongs, this, false), intent);
	}

	private void reloadShuffleList(final int numberOfSongs, final Intent intent) {
		startShuffleListProcess(new NumberOfSongs(numberOfSongs, this, true), intent);
	}

	private void startShuffleListProcess(final NumberOfSongs numberOfSongs, final Intent intent) {
		resetShuffleProgress(numberOfSongs);
		new ShuffleListProcess(new ProgressMonitor<ShuffleProgress>() {
			@Override
			public void updateProgress(final ShuffleProgress shuffleProgress) {
				updateShuffleProgressAccess(shuffleProgress, numberOfSongs);
				updateDuration(shuffleProgress, numberOfSongs);
				sendProgressUpdate(shuffleProgress, numberOfSongs, intent);
			}
		}).start(numberOfSongs);
	}

	private void resetShuffleProgress(final NumberOfSongs numberOfSongs) {
		updateShuffleProgressAccess(new FinalizationStep(), numberOfSongs);
	}

	private void updateShuffleProgressAccess(final ShuffleProgress shuffleProgress, final NumberOfSongs numberOfSongs) {
		new ShuffleProgressAccess(numberOfSongs.context).updateProgress(shuffleProgress, numberOfSongs.value);
	}

	private void updateDuration(final ShuffleProgress shuffleProgress, final NumberOfSongs numberOfSongs) {
		if (shuffleProgress == PreparationStep.DETERMINED_SONGS) {
			new DurationsAccess(this).clear();
		} else if (shuffleProgress instanceof CopySongStep) {
			int index = ((CopySongStep) shuffleProgress).getIndex();
			if (index > 0) {
				new DurationsAccess(this).updateForSong(index-1);
			}
		} else if (shuffleProgress instanceof FinalizationStep) {
			new DurationsAccess(this).updateForSong(numberOfSongs.value - 1);
		}
	}

	private void sendProgressUpdate(final ShuffleProgress shuffleProgress, final NumberOfSongs numberOfSongs,
									final Intent intent) {
		new NotificationController().updateNotifications(this, shuffleProgress, numberOfSongs);
		broadcastUpdate(intent);
	}

	private void broadcastUpdate(final Intent intent) {
		broadcaster.sendBroadcastSync(intent);
	}
}
