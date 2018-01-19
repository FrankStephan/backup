package com.frozen_foo.shuffle_my_music_app.volume;

import android.view.MotionEvent;

/**
 * Created by Frank on 14.01.2018.
 */

public class TouchDownProcessor {

	public boolean process(int action) {
		if (MotionEvent.ACTION_UP == action) {
			return true;
		} else {
			return false;
		}
	}
}
