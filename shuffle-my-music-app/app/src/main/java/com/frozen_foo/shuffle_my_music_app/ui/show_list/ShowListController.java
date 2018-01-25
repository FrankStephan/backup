package com.frozen_foo.shuffle_my_music_app.ui.show_list;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.GenericRowAdapter;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;

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
		Context          context = activity.getApplicationContext();
		List<IndexEntry> indexEntries = localIndex(activity);
		RowModel[] rows = new IndexEntryRowModelConverter().toRowModels(indexEntries);

		setDuration(activity, durations, rows);

		GenericRowAdapter adapter = new GenericRowAdapter(activity, rows);
		shuffleList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void setDuration(Activity activity, final int[] durations, final RowModel[] rows) {
		for (int i = 0; i < durations.length; i++) {
			int duration = durations[i];
			if (duration >= 0) {
				rows[i].setDuration(timeFormat().format(new Date(duration)));
			} else {
				rows[i].setDuration(activity.getString(R.string.file_corrupted));
			}
		}
	}

	public void markAsPlayingSong(Activity activity, ListView shuffleList, int index) {
		GenericRowAdapter adapter = (GenericRowAdapter) shuffleList.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			adapter.getItem(i).setPlaying(i == index);
		}

		adapter.notifyDataSetChanged();
	}

	private int[] extractDurations(final Activity activity, final RowModel[] rows) {
		int[] durations = new int[rows.length];
		for (int i = 0; i < rows.length; i++) {
			Date date = null;
			try {
				date = timeFormat().parse(rows[i].getDuration());
			} catch (ParseException e) {
				alertException(activity, e);
			}
			durations[i] = (int) date.getTime();
		}
		return durations;
	}
}
