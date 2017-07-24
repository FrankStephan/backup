package com.frozen_foo.shuffle_my_music_app;

/**
 * Created by Frank on 24.07.2017.
 */

public abstract class AsyncCallback<T> {

	private Exception exception;

	public abstract void invoke(T result);

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
