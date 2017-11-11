package com.frozen_foo.shuffle_my_music_app.ui.show_list;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;

import java.util.List;

/**
 * Created by Frank on 04.08.2017.
 */

public class ShowListController extends AbstractListController {

	public void loadAndInflateList(Activity activity, ListView shuffleList) {
		List<IndexEntry>   indexEntries = null;
		try {
			indexEntries = new ShuffleAccess().getLocalIndex(activity.getApplicationContext());
		} catch (SettingsAccessException e) {
			alertException(activity.getApplicationContext(), e);
		}
		RowModel[]         rows         = new IndexEntryRowModelConverter().toRowModels(indexEntries);
		ShowListRowAdapter adapter      = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void markAsPlayingSong(Activity activity, ListView shuffleList, int index) {
		ListAdapter       adapter = shuffleList.getAdapter();
		if (! (adapter instanceof  ShowListRowAdapter)) {
			loadAndInflateList(activity, shuffleList);
		}
		adapter = shuffleList.getAdapter();
		RowModel[]         rows     = rowsFrom(adapter);
		for (int i = 0; i < rows.length; i++) {
			rows[i].setPlaying(i == index);
		}
		((ShowListRowAdapter)adapter).notifyDataSetChanged();
	}
}
