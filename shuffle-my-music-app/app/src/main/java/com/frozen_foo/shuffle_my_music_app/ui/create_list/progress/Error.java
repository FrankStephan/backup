package com.frozen_foo.shuffle_my_music_app.ui.create_list.progress;

/**
 * Created by Frank on 15.02.2018.
 */

public class Error implements ShuffleProgress {

	private final Exception exception;

	public Error(final Exception exception) {
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}
}
