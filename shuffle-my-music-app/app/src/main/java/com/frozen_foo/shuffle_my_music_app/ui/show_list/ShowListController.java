package com.frozen_foo.shuffle_my_music_app.ui.show_list;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank on 04.08.2017.
 */

public class ShowListController extends AbstractListController {

	@NonNull
	private SimpleDateFormat timeFormat() {
		return new SimpleDateFormat("mm:ss");
	}

	public void loadAndInflateList(Activity activity, ListView shuffleList, final int[] durations) {
		List<IndexEntry>   indexEntries = null;
		try {
			indexEntries = new ShuffleAccess().getLocalIndex(activity.getApplicationContext());
		} catch (SettingsAccessException e) {
			alertException(activity.getApplicationContext(), e);
		}
		RowModel[]         rows         = new IndexEntryRowModelConverter().toRowModels(indexEntries);
		for (int i = 0; i < durations.length; i++) {
			rows[i].setDuration(timeFormat().format(new Date(durations[i])));
		}
		ShowListRowAdapter adapter      = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void markAsPlayingSong(Activity activity, ListView shuffleList, int index) {
		ListAdapter       adapter = shuffleList.getAdapter();
		RowModel[] rows = rowsFrom(adapter);
		if (! (adapter instanceof  ShowListRowAdapter)) {
			int[] durations = extractDurations(activity, rows);
			loadAndInflateList(activity, shuffleList, durations);
		}
		adapter = shuffleList.getAdapter();
		for (int i = 0; i < rows.length; i++) {
			rows[i].setPlaying(i == index);
		}
		((ShowListRowAdapter)adapter).notifyDataSetChanged();
	}

	private int[] extractDurations(final Activity activity, final RowModel[] rows) {
		int[] durations = new int[rows.length];
		for (int i = 0; i < rows.length; i++) {
			Date date = null;
			try {
				date = timeFormat().parse(rows[i].getDuration());
			} catch (ParseException e) {
				alertException(activity.getApplicationContext(), e);
			}
			durations[i] = (int) date.getTime();
		}
		return durations;
	}
}
