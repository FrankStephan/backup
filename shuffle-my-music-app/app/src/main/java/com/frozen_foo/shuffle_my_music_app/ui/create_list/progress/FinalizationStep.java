package com.frozen_foo.shuffle_my_music_app.ui.create_list.progress;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;

import java.util.List;

/**
 * Created by Frank on 05.08.2017.
 */

public class FinalizationStep implements ShuffleProgress {

	private final List<IndexEntry> songs;

	public FinalizationStep(final List<IndexEntry> songs) {
		this.songs = songs;
	}

	public List<IndexEntry> getSongs() {
		return songs;
	}


}
