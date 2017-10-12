package com.frozen_foo.shuffle_my_music_app.main.select_favorites;

import android.app.Activity;
import android.database.DataSetObserver;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.main.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.main.RowModel;
import com.frozen_foo.shuffle_my_music_app.main.show_list.ShowListRowAdapter;

import org.apache.commons.lang3.ArrayUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Frank on 24.08.2017.
 */

public class SelectFavoritesController {

	public void selectFavorites(Activity activity, ListView shuffleList, DataSetObserver selectionChangeObserver) {
		final ListAdapter          adapter              = shuffleList.getAdapter();
		RowModel[]                 rows                 = rowsFrom(adapter);
		final SelectableRowAdapter selectableRowAdapter = new SelectableRowAdapter(activity, rows);
		selectableRowAdapter.registerDataSetObserver(selectionChangeObserver);
		shuffleList.setAdapter(selectableRowAdapter);
		selectableRowAdapter.notifyDataSetChanged();
	}

	public boolean atLeastOneSelected(ListView shuffleList) {
		RowModel[] rows = rowsFrom(shuffleList.getAdapter());
		for (RowModel row : rows) {
			if (row.isFavorite()) {
				return true;
			}
		}
		return false;
	}

	private RowModel[] rowsFrom(final ListAdapter adapter) {
		RowModel[] rows = new RowModel[adapter.getCount()];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = (RowModel) adapter.getItem(i);
		}
		return rows;
	}

	public void addFavorites(Activity activity, ListView shuffleList) {
		doCancel(activity, shuffleList);
		String         localDirPath      = new LocalDirectoryAccess().localSongsDir().getAbsolutePath();
		RowModel[]     rowModels         = rowsFrom(shuffleList.getAdapter());
		List<RowModel> selectedRowModels = new LinkedList<>();
		for (RowModel rowModel : rowModels) {
			if (rowModel.isFavorite()) {
				selectedRowModels.add(rowModel);
			}
		}

		List<IndexEntry> indexEntries = new IndexEntryRowModelConverter().toIndexEntries(selectedRowModels);

		List<IndexEntry> addedIndexEntries = new ShuffleMyMusicService().addFavorites(localDirPath, indexEntries);

		Toast.makeText(activity.getApplicationContext(), ArrayUtils.toString(addedIndexEntries), Toast.LENGTH_LONG)
				.show();
	}

	public void cancelFavoritesSelection(Activity activity, ListView shuffleList) {
		doCancel(activity, shuffleList);
	}

	private void doCancel(final Activity activity, final ListView shuffleList) {
		final SelectableRowAdapter adapter = (SelectableRowAdapter) shuffleList.getAdapter();
		RowModel[]                 rows    = rowsFrom(adapter);
		adapter.release();
		ShowListRowAdapter showListRowAdapter = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(showListRowAdapter);
	}
}
