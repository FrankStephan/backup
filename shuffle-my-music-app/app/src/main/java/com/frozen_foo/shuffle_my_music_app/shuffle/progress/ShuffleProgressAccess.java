package com.frozen_foo.shuffle_my_music_app.shuffle.progress;

import android.content.Context;
import android.content.SharedPreferences;

import com.frozen_foo.shuffle_my_music_app.Logger;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.Error;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;
import com.google.gson.Gson;

/**
 * Created by Frank on 19.02.2018.
 */

public class ShuffleProgressAccess {


	private static final String PREFS_NAME = "shuffleProgressTempData";
	private static final String SHUFFLE_LIST_STEP = "shuffle_list_step";
	private static final String NUMBER_OF_SONGS = "number_of_songs";
	private static final String SHUFFLE_LIST_EXCEPTION = "shuffle_list_exception";
	private static final String SHUFFLE_LIST_EXCEPTION_CLASS = "shuffle_list_exception_class";
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

		String exceptionJsonString = null;
		String exceptionClassName  = null;
		if (shuffleProgress instanceof Error) {
			Gson      gson      = new Gson();
			Exception exception = ((Error) shuffleProgress).getException();
			exceptionJsonString = gson.toJson(exception);
			exceptionClassName = exception.getClass().getName();
		}

		sharedPreferences().edit().putInt(SHUFFLE_LIST_STEP, shuffleListStep).putInt(NUMBER_OF_SONGS, numberOfSongs)
				.putString(SHUFFLE_LIST_EXCEPTION, exceptionJsonString)
				.putString(SHUFFLE_LIST_EXCEPTION_CLASS, exceptionClassName).apply();
	}

	private SharedPreferences sharedPreferences() {
		return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	private int toInt(ShuffleProgress shuffleProgress) {
		if (shuffleProgress != null) {
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
		} else{
			return 0;
		}
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
				Class<? extends Exception> exceptionClass = null;
				String exceptionClassName =
						sharedPreferences().getString(SHUFFLE_LIST_EXCEPTION_CLASS, Exception.class.getName());
				try {

					exceptionClass = Class.forName(exceptionClassName).asSubclass(Exception.class);
				} catch (ClassNotFoundException e) {
					Logger.logException(context, e);
				}
				Gson gson = new Gson();
				String exceptionJsonString = sharedPreferences().getString(SHUFFLE_LIST_EXCEPTION, null);
				Exception e = gson.fromJson(exceptionJsonString, exceptionClass);
				return new Error(e);
			default:
				return new CopySongStep(i - 5);
		}
	}
}
