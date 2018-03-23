package com.frozen_foo.shuffle_my_music_app;

import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

/**
 * Created by Frank on 19.03.2018.
 */

public abstract class ProgressBarUpdate {

	private final int numberOfSongs;

	public ProgressBarUpdate(final int numberOfSongs) {
		this.numberOfSongs = numberOfSongs;
	}

	public void update(ShuffleProgress shuffleProgress) {
		int progressIndex = toProgressIndex(shuffleProgress);
		update(progressIndex);
	}

	private int progressMax() {
		return 4 + numberOfSongs;
	}

	private int toProgressIndex(ShuffleProgress shuffleProgress) {
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

	protected abstract void prepare(int max);

	protected abstract void update(int progressIndex);

	protected abstract void finish();




}
