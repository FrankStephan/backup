package com.frozen_foo.shuffle_my_music_app.shuffle.progress;

import android.content.Context;
import android.content.SharedPreferences;

import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.Error;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

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

	public ShuffleProgress mostRecentProgress() {
		int             shuffleListStep = sharedPreferences().getInt(SHUFFLE_LIST_STEP, 0);
		ShuffleProgress shuffleProgress = fromInt(shuffleListStep);
		return shuffleProgress;
	}

	public int numberOfSongs() {
		int numberOfSongs = sharedPreferences().getInt(NUMBER_OF_SONGS, 0);
		return numberOfSongs;
	}

	public void updateProgress(ShuffleProgress shuffleProgress, final int numberOfSongs) {
		int shuffleListStep = toInt(shuffleProgress);
		sharedPreferences().edit().putInt(SHUFFLE_LIST_STEP, shuffleListStep).putInt(NUMBER_OF_SONGS, numberOfSongs)
				.apply();
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
				return new FinalizationStep();
			case 1:
				return PreparationStep.SAVING_FAVORITES;
			case 2:
				return PreparationStep.LOADING_INDEX;
			case 3:
				return PreparationStep.SHUFFLING_INDEX;
			case 4:
				return PreparationStep.DETERMINED_SONGS;
			case -1:
				return new Error(null); // TODO: Store exception inside the intent
			default:
				return new CopySongStep(i - 5);
		}
	}
}
