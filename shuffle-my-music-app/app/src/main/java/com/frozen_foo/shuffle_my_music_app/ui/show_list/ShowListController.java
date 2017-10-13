package com.frozen_foo.shuffle_my_music_app.ui.show_list;

import android.app.Activity;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;

import java.util.List;

/**
 * Created by Frank on 04.08.2017.
 */

public class ShowListController {

	public void loadAndInflateList(Activity activity, ListView shuffleList) {
		List<IndexEntry>   indexEntries = new ShuffleAccess().getLocalIndex();
		RowModel[]         rows         = new IndexEntryRowModelConverter().toRowModels(indexEntries);
		ShowListRowAdapter adapter      = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(adapter);
	}
}
