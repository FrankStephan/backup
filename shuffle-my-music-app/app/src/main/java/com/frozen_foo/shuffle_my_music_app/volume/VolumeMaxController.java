package com.frozen_foo.shuffle_my_music_app.volume;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;

/**
 * Created by Frank on 04.01.2018.
 */

public class VolumeMaxController {

	public static final int MIN_TOUCH_TIME_MILLIS = 1000;
	public static final int UPDATE_INTERVAL_MILLIS = 100;
	private VolumeChangeObserver volumeChangeObserver;
	private TouchEventHandler touchEventHandler;

	@SuppressLint("ClickableViewAccessibility")
	public void init(final Activity activity, MenuItem menuItem, final ProgressBar progressBar) {
		TextView textView = initWidget(activity, menuItem, progressBar);
		registerVolumeChangeListener(activity, textView);
	}

	public void release(Activity activity) {
		unregisterVolumeChangeListener(activity);
	}

	private TextView initWidget(final Activity activity, final MenuItem menuItem, final ProgressBar progressBar) {
		TextView textView = new TextView(activity);
		textView.setText(R.string.v_max);
		menuItem.setActionView(textView);

		touchEventHandler = new TouchEventHandler(progressBar, MIN_TOUCH_TIME_MILLIS, UPDATE_INTERVAL_MILLIS,
				changeVolumeCallback(activity), determineReactionMode(activity));
		textView.setOnTouchListener(touchEventHandler);

		updateWidgetState(activity, textView);
		return textView;
	}

	@NonNull
	private AsyncCallback<Boolean> changeVolumeCallback(final Activity activity) {
		return new AsyncCallback<Boolean>() {
			@Override
			public void invoke(final Boolean successful) {

				switch (determineReactionMode(activity)) {
					case LONG_TOUCH:
						if (successful) {
							increaseVolumeToMax(activity);
						} else {
							alertUserNeedsToTouchLonger(activity);
						}
						break;

					case DIRECT:
						if (successful) {
							decreaseVolumeToNormal(activity);
						} else {
							System.err.println("######## failed");
						}
						break;
				}
			}
		};
	}

	private ReactionMode determineReactionMode(Activity activity) {
		return checkVolumeMax(activity) ? ReactionMode.DIRECT : ReactionMode.LONG_TOUCH;
	}

	private boolean checkVolumeMax(Activity activity) {
		AudioManager audioManager = audioManager(activity);
		return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) == audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	private AudioManager audioManager(final Activity activity) {
		return activity.getSystemService(AudioManager.class);
	}

	private void increaseVolumeToMax(Activity activity) {
		AudioManager audioManager = audioManager(activity);
		audioManager
				.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
						AudioManager.FLAG_SHOW_UI);
	}

	private void decreaseVolumeToNormal(final Activity activity) {
		AudioManager audioManager = audioManager(activity);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				(int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 1.5), AudioManager.FLAG_SHOW_UI);
	}

	private void alertUserNeedsToTouchLonger(final Activity activity) {
		Toast.makeText(activity, R.string.touch_longer_notification, Toast.LENGTH_SHORT).show();
	}

	private void updateWidgetState(Activity activity, TextView textView) {
		int textColor = checkVolumeMax(activity) ? android.R.color.holo_blue_dark : android.R.color.black;
		textView.setTextColor(activity.getResources().getColor(textColor, activity.getTheme()));
	}

	private void registerVolumeChangeListener(final Activity activity, final TextView textView) {
		volumeChangeObserver = new VolumeChangeObserver(new Handler(), audioManager(activity), new VolumeChangeListener() {
			@Override
			public void volumeChanged() {
				updateWidgetState(activity, textView);
				touchEventHandler.setReactionMode(determineReactionMode(activity));
			}
		});
		activity.getContentResolver()
				.registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, volumeChangeObserver);
	}

	private void unregisterVolumeChangeListener(Activity activity) {
		activity.getContentResolver().unregisterContentObserver(volumeChangeObserver);
	}

}
