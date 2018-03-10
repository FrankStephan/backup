package com.frozen_foo.shuffle_my_music_app.shuffle.progress.steps;

/**
 * Created by Frank on 06.08.2017.
 */

public class CopySongStep implements ShuffleProgress {

	private final int index;

	public CopySongStep(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
