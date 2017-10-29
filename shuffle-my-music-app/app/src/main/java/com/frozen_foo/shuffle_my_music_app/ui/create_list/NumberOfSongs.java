package com.frozen_foo.shuffle_my_music_app.ui.create_list;

import android.content.Context;

/**
 * Created by Frank on 01.08.2017.
 */

public class NumberOfSongs {

	public int value;
	public Context context;
	public boolean useExistingList;

	public NumberOfSongs(int value, Context context, boolean useExistingList) {
		this.value = value;
		this.context = context;
		this.useExistingList = useExistingList;
	}
}
