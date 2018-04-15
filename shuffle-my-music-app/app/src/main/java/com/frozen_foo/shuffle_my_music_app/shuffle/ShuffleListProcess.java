package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.async.ProgressMonitor;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps.ShuffleProgress;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Frank on 01.08.2017.
 */

public class ShuffleListProcess extends AbstractListProcess {


	public ShuffleListProcess(ProgressMonitor<ShuffleProgress> progressMonitor) {
		super(progressMonitor);
	}

	@Override
	protected List<IndexEntry> loadNewList(final Context context, InputStream indexStream, int numberOfSongs) {
		return shuffleAccess().shuffleIndexEntries(indexStream, numberOfSongs);
	}

}
