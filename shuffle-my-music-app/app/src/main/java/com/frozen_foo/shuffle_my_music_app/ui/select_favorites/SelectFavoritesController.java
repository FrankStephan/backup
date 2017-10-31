package com.frozen_foo.shuffle_my_music_app.ui.select_favorites;

import android.app.Activity;
import android.database.DataSetObserver;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;
import com.frozen_foo.shuffle_my_music_app.ui.show_list.ShowListRowAdapter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Frank on 24.08.2017.
 */

public class SelectFavoritesController extends AbstractListController {

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

	public void addFavorites(Activity activity, ListView shuffleList) {
		doCancel(activity, shuffleList);

		RowModel[]     rowModels         = rowsFrom(shuffleList.getAdapter());
		List<RowModel> selectedRowModels = new LinkedList<>();
		for (RowModel rowModel : rowModels) {
			if (rowModel.isFavorite()) {
				selectedRowModels.add(rowModel);
			}
		}

		List<IndexEntry> indexEntries = new IndexEntryRowModelConverter().toIndexEntries(selectedRowModels);
		List<IndexEntry> addedIndexEntries = new ShuffleAccess().markAsFavorites(indexEntries);
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
