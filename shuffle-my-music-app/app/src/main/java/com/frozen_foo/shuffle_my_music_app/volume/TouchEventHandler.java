package com.frozen_foo.shuffle_my_music_app.volume;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_app.async.AsyncCallback;

/**
 * Created by Frank on 04.01.2018.
 */

public class TouchEventHandler implements View.OnTouchListener {

	private final ProgressBar progressBar;
	private final int minTouchTimeMillis;
	private final int updateIntervalMillis;
	private final AsyncCallback<Boolean> callback;

	private ReactionMode reactionMode;

	private AsyncProgressBarTask asyncProgressBarTask;
	private LongTouchProcessor longTouchProcessor = new LongTouchProcessor();
	private TouchDownProcessor touchDownProcessor = new TouchDownProcessor();


	public TouchEventHandler(final ProgressBar progressBar, final int minTouchTimeMillis,
							 final int updateIntervalMillis, final AsyncCallback<Boolean> callback,
							 ReactionMode reactionMode) {
		this.progressBar = progressBar;
		this.minTouchTimeMillis = minTouchTimeMillis;
		this.updateIntervalMillis = updateIntervalMillis;
		this.callback = callback;
		this.reactionMode = reactionMode;
	}

	public ReactionMode getReactionMode() {
		return reactionMode;
	}

	public void setReactionMode(final ReactionMode reactionMode) {
		this.reactionMode = reactionMode;
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		switch (reactionMode) {
			case LONG_TOUCH:
				longTouchProcessor
						.process(event.getAction(), minTouchTimeMillis, updateIntervalMillis, progressBar, callback);
				break;
			case DIRECT:
				boolean result = touchDownProcessor.process(event.getAction());
				callback.invoke(result);
				break;
		}
		return true;
	}
}
