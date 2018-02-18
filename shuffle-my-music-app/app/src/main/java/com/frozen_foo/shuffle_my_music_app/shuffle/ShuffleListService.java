package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.ui.ShuffleListActivity;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.NumberOfSongs;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.StartSongCopyStep;

public class ShuffleListService extends IntentService {

	public static final String ACTION_CREATE_NEW_SHUFFLE_LIST =
			"com.frozen_foo.myapplication.action.CREATE_NEW_SHUFFLE_LIST";
	public static final String ACTION_RELOAD_SHUFFLE_LIST = "com.frozen_foo.myapplication.action.RELOAD_SHUFFLE_LIST";
	public static final String SHUFFLE_PROGRESS = "com.frozen_foo.myapplication.extra.SHUFFLE_PROGRESS";
	public static final int DEFAULT_NUMBER_OF_SONGS = 10;
	public static final int NOTIFICATION_ID = 1;

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

	public static ShuffleProgress extractProgress(Intent intent) {
		return (ShuffleProgress) intent.getSerializableExtra(ShuffleListService.SHUFFLE_PROGRESS);
	}

	private static void putProgress(ShuffleProgress shuffleProgress, Intent intent) {
		intent.putExtra(SHUFFLE_PROGRESS, shuffleProgress);
	}

	public static int extractNumberOfSongs(final Intent intent) {
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

		Notification notification = buildNotification("");
		startForeground(NOTIFICATION_ID, notification);
	}

	private Notification buildNotification(String contextText) {
		Intent notificationIntent = new Intent(this, ShuffleListActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		return new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_shuffle_white_24dp)
				.setContentTitle(getString(R.string.app_name)).setContentText(contextText)
				.setContentIntent(pendingIntent).build();
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
		new ShuffleListProcess(new ProgressMonitor<ShuffleProgress>() {
			@Override
			public void updateProgress(final ShuffleProgress shuffleProgress) {
				notifyProgressUpdate(shuffleProgress, numberOfSongs);
				putProgress(shuffleProgress, intent);
				broadcaster.sendBroadcastSync(intent);
			}
		}).start(numberOfSongs);
	}

	private void notifyProgressUpdate(final ShuffleProgress shuffleProgress, final NumberOfSongs numberOfSongs) {
		if (shuffleProgress instanceof PreparationStep) {
			PreparationStep preparationStep = (PreparationStep) shuffleProgress;
			switch (preparationStep) {
				case SAVING_FAVORITES:
					notify(getString(R.string.saveFavorites));
					break;
				case LOADING_INDEX:
					notify(getString(R.string.indexLoading));
					break;
				case SHUFFLING_INDEX:
					notify(getString(R.string.determineRandomSongs));
					break;
			}
		} else {
			if (shuffleProgress instanceof StartSongCopyStep) {
				String notificationMessage = new StringBuilder().append(getString(R.string.copying_title)).append(" ")
						.append(((StartSongCopyStep) shuffleProgress).getIndex() + 1).append("/")
						.append(numberOfSongs.value).toString();
				notify(notificationMessage);
			} else if (shuffleProgress instanceof FinalizationStep) {
				notificationManager().cancel(NOTIFICATION_ID);
			}
		}
	}

	private void notify(String contextText) {
		notificationManager().notify(NOTIFICATION_ID, buildNotification(contextText));
	}

	private void notify(Notification notification) {
		notificationManager().notify(NOTIFICATION_ID, notification);
	}

	private NotificationManager notificationManager() {
		return getSystemService(NotificationManager.class);
	}
}
