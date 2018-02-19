package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;
import android.content.SharedPreferences;

import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.DeterminedSongsStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinalizationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.FinishedSongCopyStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.PreparationStep;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.ShuffleProgress;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.progress.StartSongCopyStep;

/**
 * Created by Frank on 19.02.2018.
 */

public class PersistentProgressMonitor extends ProgressMonitor<ShuffleProgress> {

	private final Context context;

	public PersistentProgressMonitor(final Context context) {
		this.context = context;
	}

	@Override
	public void updateProgress(final ShuffleProgress shuffleProgress) {
		synchronized (this) {

		}
	}

	public ShuffleProgress mostRecentShuffleProgress(int index) {
		synchronized (this) {
return fromInt(index);
		}
	}

	private SharedPreferences sharedPreferences() {
		return context.getSharedPreferences("shuffleProgressPrefs", Context.MODE_PRIVATE);
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
			}
		} else {
			if (shuffleProgress instanceof DeterminedSongsStep) {
				return 4;
			} else if (shuffleProgress instanceof StartSongCopyStep) {
				int index = ((StartSongCopyStep) shuffleProgress).getIndex();
				return (PreparationStep.values().length + index);
			} else if (shuffleProgress instanceof FinishedSongCopyStep) {
				int index = ((StartSongCopyStep) shuffleProgress).getIndex();
				return (PreparationStep.values().length + index);
			} else if (shuffleProgress instanceof FinalizationStep) {
				return 0;
			}
		}
		return -1;
	}

	private ShuffleProgress fromInt(int i) {
		switch (i) {
			case 1: return PreparationStep.SAVING_FAVORITES;
			case 2: return PreparationStep.LOADING_INDEX;
			case 3: return PreparationStep.SHUFFLING_INDEX;
			case 4:
			case 0:
			case -1:
			default:
				return new StartSongCopyStep(i);
		}
	}

}
