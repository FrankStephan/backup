package com.frozen_foo.shuffle_my_music_app;

import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressRunnable;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.CopySongStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

/**
 * Created by Frank on 19.03.2018.
 */

public abstract class ProgressUIUpdate implements ShuffleProgressRunnable {

	@Override
	public void run(final ShuffleProgress shuffleProgress, final int numberOfSongs) {
		defineMax(PreparationStep.values().length + numberOfSongs);
		int progressIndex = toProgressIndex(shuffleProgress);
		update(progressIndex);
		if (0 == progressIndex) {
			finish();
		}
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



	protected abstract void showText(String text);

	protected abstract void defineMax(int max);

	protected abstract void update(int progressIndex);

	protected abstract void finish();

}
