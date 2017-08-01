package com.frozen_foo.shuffle_my_music_app.async;

/**
 * Created by Frank on 01.08.2017.
 */

public abstract class ProgressMonitor<Progress> {

	public abstract void updateProgress(Progress progress);

}
