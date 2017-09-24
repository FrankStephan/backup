package com.frozen_foo.shuffle_my_music_app.main.show_list;

import android.app.Activity;
import android.content.Context;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.main.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.main.RowModel;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Frank on 04.08.2017.
 */

public class ShowListController {

	public IndexEntry[] loadAndInflateList(Activity activity, ListView shuffleList) {
		RowModel[] rows = rowsFrom(shuffleList.getAdapter());
		ShowListRowAdapter adapter = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(adapter);
		return new IndexEntryRowModelConverter().toIndexEntries(rows);
	}

	private RowModel[] rowsFrom(final ListAdapter adapter) {
		if (adapter != null) {
			RowModel[] rows = new RowModel[adapter.getCount()];
			for (int i = 0; i < rows.length; i++) {
				rows[i] = (RowModel) adapter.getItem(i);
			}
			return rows;
		} else {
			return new RowModel[0];
		}
	}


}
