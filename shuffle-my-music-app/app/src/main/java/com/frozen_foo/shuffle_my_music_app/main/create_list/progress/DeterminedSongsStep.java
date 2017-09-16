package com.frozen_foo.shuffle_my_music_app.main.create_list.progress;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;

/**
 * Created by Frank on 06.08.2017.
 */

public class DeterminedSongsStep implements ShuffleProgress  {

	private final IndexEntry[] songs;

	public DeterminedSongsStep(final IndexEntry[] songs) {
		this.songs = songs;
	}

	public IndexEntry[] getSongs() {
		return songs;
	}
}
