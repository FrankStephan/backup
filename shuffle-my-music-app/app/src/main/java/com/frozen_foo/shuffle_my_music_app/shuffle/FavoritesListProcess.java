package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Frank on 13.04.2018.
 */

public class FavoritesListProcess extends AbstractListProcess {

	public FavoritesListProcess(final ProgressMonitor<ShuffleProgress> progressMonitor) {
		super(progressMonitor);
	}

	@Override
	protected List<IndexEntry> loadNewList(final Context context, final InputStream indexStream, final int numberOfSongs) {
		return shuffleAccess().loadLocalFavorites(context);
	}
}
