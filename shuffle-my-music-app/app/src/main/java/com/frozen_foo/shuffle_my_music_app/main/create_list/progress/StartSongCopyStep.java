package com.frozen_foo.shuffle_my_music_app.main.create_list.progress;

/**
 * Created by Frank on 06.08.2017.
 */

public class StartSongCopyStep implements ShuffleProgress {

	private final int index;

	public StartSongCopyStep(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
