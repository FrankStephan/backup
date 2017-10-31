package com.frozen_foo.shuffle_my_music_app.ui;

import android.widget.ListAdapter;

/**
 * Created by Frank on 31.10.2017.
 */

public abstract class AbstractListController {
	protected RowModel[] rowsFrom(final ListAdapter adapter) {
		RowModel[] rows = new RowModel[adapter.getCount()];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = (RowModel) adapter.getItem(i);
		}
		return rows;
	}
}
