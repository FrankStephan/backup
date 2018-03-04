package com.frozen_foo.shuffle_my_music_app.shuffle.progress;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

/**
 * Created by Frank on 19.02.2018.
 */

public class ShuffleProgressAccess {

	private static final String PREFS_NAME = "shuffleProgressTempData";
	private static final String SHUFFLE_LIST_STEP = "shuffle_list_step";
	private static final String NUMBER_OF_SONGS = "number_of_songs";

	private final Context context;

	public ShuffleProgressAccess(final Context context) {
		this.context = context;
	}

	public void updateShuffleProgress(final ShuffleProgress shuffleProgress, final int numberOfSongs,
									  final ShuffleProgressRunnable runnable, Handler handler) {
		synchronized (getClass()) {
			int shuffleListStep = toInt(shuffleProgress);
			sharedPreferences().edit().putInt(SHUFFLE_LIST_STEP, shuffleListStep).putInt(NUMBER_OF_SONGS, numberOfSongs)
					.apply();
			handler.post(new Runnable() {
				@Override
				public void run() {
					runnable.run(shuffleProgress, numberOfSongs);
				}
			});
		}
	}

	public void runWithMostRecentShuffleProgress(final ShuffleProgressRunnable runnable) {
		synchronized (getClass()) {
			int             shuffleListStep = sharedPreferences().getInt(SHUFFLE_LIST_STEP, 0);
			ShuffleProgress shuffleProgress = fromInt(shuffleListStep);
			int             numberOfSongs   = sharedPreferences().getInt(NUMBER_OF_SONGS, 0);

			// TODO: Run only during shuffle

			runnable.run(shuffleProgress, numberOfSongs);
		}
	}

	private SharedPreferences sharedPreferences() {
		return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	private int toInt(ShuffleProgress shuffleProgress) {
		if (shuffleProgress instanceof PreparationStep) {
			PreparationStep preparationStep = (PreparationStep) shuffleProgress;
			switch (preparationStep) {
				case SAVING_FAVORITES:
					return 1;
				case LOADING_INDEX:
					return 2;
				case SHUFFLING_INDEX:
					return 3;
				case DETERMINED_SONGS:
					return 4;
			}
		} else {
			if (shuffleProgress instanceof CopySongStep) {
				int index = ((CopySongStep) shuffleProgress).getIndex();
				return (5 + index);
			} else if (shuffleProgress instanceof FinalizationStep) {
				return 0;
			}
		}
		return -1;
	}

	private ShuffleProgress fromInt(int i) {
		switch (i) {
			case 0:
				return null;
			case 1:
				return PreparationStep.SAVING_FAVORITES;
			case 2:
				return PreparationStep.LOADING_INDEX;
			case 3:
				return PreparationStep.SHUFFLING_INDEX;
			case 4:
				return PreparationStep.DETERMINED_SONGS;
			case -1:
				return new Error(null);
			default:
				return new CopySongStep(i - 5);
		}
	}
}
