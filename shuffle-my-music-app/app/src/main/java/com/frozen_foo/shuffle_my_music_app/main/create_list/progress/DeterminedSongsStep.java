package com.frozen_foo.shuffle_my_music_app.main.create_list.progress;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;

import java.util.List;

/**
 * Created by Frank on 06.08.2017.
 */

public class DeterminedSongsStep implements ShuffleProgress  {

	private final List<IndexEntry> songs;

	public DeterminedSongsStep(final List<IndexEntry> songs) {
		this.songs = songs;
	}

	public List<IndexEntry> getSongs() {
		return songs;
	}
}
