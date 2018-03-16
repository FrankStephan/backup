package com.frozen_foo.shuffle_my_music_app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.ListAdapter;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.Logger;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

	protected void alertException(Activity activity, Exception e) {
		Logger.logException(activity, e);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity).setTitle(e.getClass().getSimpleName())
				.setMessage(e.getMessage());

		AlertDialog         dialog  = builder.create();
		dialog.show();
	}

	protected List<IndexEntry> localIndex(Activity activity) {
		return new ShuffleAccess().getLocalIndex(activity);
	}
}
