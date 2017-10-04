package com.frozen_foo.shuffle_my_music_app.main.show_list;

import android.app.Activity;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.main.RowModel;
import com.frozen_foo.shuffle_my_music_app.main.IndexEntryRowModelConverter;

/**
 * Created by Frank on 04.08.2017.
 */

public class ShowListController {

	public void loadAndInflateList(Activity activity, ListView shuffleList) {
		String             localDirPath = new LocalDirectoryAccess().localDir().getPath();
		IndexEntry[]       indexEntries = new ShuffleMyMusicService().loadSongsFile(localDirPath);
		RowModel[]         rows         = new IndexEntryRowModelConverter().toRowModels(indexEntries);
		ShowListRowAdapter adapter      = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(adapter);
	}
}
