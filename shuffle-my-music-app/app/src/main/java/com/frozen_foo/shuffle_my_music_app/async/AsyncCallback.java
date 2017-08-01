package com.frozen_foo.shuffle_my_music_app.async;

/**
 * Created by Frank on 24.07.2017.
 */

public abstract class AsyncCallback<Result> {

	private Exception exception;

	public abstract void invoke(Result result);

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public boolean hasException() {
		return exception != null;
	}
}
