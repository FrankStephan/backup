package com.frozen_foo.shuffle_my_music_app.main.show_list;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.main.RowModel;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Frank on 04.08.2017.
 */

public class ShowListController {
	
	private Activity activity;
	private Context context;

	public ShowListController(final Activity activity, final Context context) {
		this.activity = activity;
		this.context = context;
	}

	public File[] loadAndInflateList(File shuffleMyMusicDir) {
		ListView shuffleList = (ListView) activity.findViewById(R.id.shuffleList);

		File[] songs = shuffleMyMusicDir.listFiles();
		Arrays.sort(songs);
		RowModel[] rows = new RowModel[songs.length];
		for (int i = 0; i < songs.length; i++) {
			rows[i] = new RowModel(songs[i].getName(), songs[i].getPath(), false);
		}

		ShowListRowAdapter adapter = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(adapter);
		return songs;
	}


}
