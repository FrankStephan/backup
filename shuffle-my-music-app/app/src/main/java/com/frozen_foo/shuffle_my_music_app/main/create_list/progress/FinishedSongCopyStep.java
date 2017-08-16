package com.frozen_foo.shuffle_my_music_app.main.create_list.progress;

/**
 * Created by Frank on 06.08.2017.
 */

public class FinishedSongCopyStep implements ShuffleProgress {

	private final int index;

	public FinishedSongCopyStep(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
