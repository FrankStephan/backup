package com.frozen_foo.shuffle_my_music_app.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ListAdapter;

import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;

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

	protected void alertException(Context context, Exception e) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(e.getClass().getSimpleName())
				.setMessage(e.getCause().getMessage());
		AlertDialog         dialog  = builder.create();
		dialog.show();
		// Replace by log4j
		e.printStackTrace();
	}
}
