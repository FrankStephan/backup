package com.frozen_foo.shuffle_my_music_app.ui.show_list;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
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
		List<IndexEntry>   indexEntries = new ShuffleAccess().getLocalIndex();
		RowModel[]         rows         = new IndexEntryRowModelConverter().toRowModels(indexEntries);
		ShowListRowAdapter adapter      = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(adapter);
	}

	public void markAsPlayingSong(ListView shuffleList, int index) {
		ShowListRowAdapter adapter = (ShowListRowAdapter) shuffleList.getAdapter();
		RowModel[]  rows    = rowsFrom(adapter);
		for (int i = 0; i < rows.length; i++) {
			rows[i].setPlaying(i == index);
		}
		adapter.notifyDataSetChanged();
	}
}
