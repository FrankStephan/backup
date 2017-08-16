package com.frozen_foo.shuffle_my_music_app.main.create_list.progress;

/**
 * Created by Frank on 06.08.2017.
 */

public class DeterminedSongsStep implements ShuffleProgress  {

	private final String[] songs;

	public DeterminedSongsStep(final String[] songs) {
		this.songs = songs;
	}

	public String[] getSongs() {
		return songs;
	}
}
