package com.frozen_foo.shuffle_my_music_app.ui.select_favorites;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.ui.AbstractListController;
import com.frozen_foo.shuffle_my_music_app.ui.GenericRowAdapter;
import com.frozen_foo.shuffle_my_music_app.ui.IndexEntryRowModelConverter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Frank on 24.08.2017.
 */

public class SelectFavoritesController extends AbstractListController {

	public void selectFavorites(Activity activity, ListView shuffleList) {
		Context          context      = activity.getApplicationContext();
		List<IndexEntry> indexEntries = localIndex(activity);
		RowModel[]       rows         = new IndexEntryRowModelConverter().toRowModels(indexEntries);

		List<IndexEntry> markedFavorites = loadMarkedFavorites(activity);


		GenericRowAdapter adapter = (GenericRowAdapter) shuffleList.getAdapter();
		checkFavorites(indexEntries, markedFavorites, adapter);
		adapter.setShowFavoritesSelection(true);
		adapter.setShowDurations(false);
		adapter.notifyDataSetChanged();
	}

	private List<IndexEntry> loadMarkedFavorites(Activity activity) {
		try {
			return new ShuffleAccess().loadMarkedFavorites(activity);
		} catch (IOException e) {
			alertException(activity, e);
			return Collections.emptyList();
		}
	}

	private void checkFavorites(final List<IndexEntry> indexEntries, final List<IndexEntry> markedFavorites,
								GenericRowAdapter adapter) {
		for (int i = 0; i < indexEntries.size(); i++) {
			adapter.getItem(i).setFavorite(markedFavorites.contains(indexEntries.get(i)));
		}
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

	public void markSelectedFavorites(Activity activity, ListView shuffleList) {
		doCancel(activity, shuffleList);

		RowModel[]     rowModels         = rowsFrom(shuffleList.getAdapter());
		List<RowModel> selectedRowModels = new LinkedList<>();
		for (RowModel rowModel : rowModels) {
			if (rowModel.isFavorite()) {
				selectedRowModels.add(rowModel);
			}
		}

		List<IndexEntry> indexEntries      = new IndexEntryRowModelConverter().toIndexEntries(selectedRowModels);
		List<IndexEntry> addedIndexEntries = null;
		try {
			addedIndexEntries = new ShuffleAccess().markAsFavorites(activity.getApplicationContext(), indexEntries);
		} catch (IOException e) {
			alertException(activity, e);
		}
		Toast.makeText(activity.getApplicationContext(), ArrayUtils.toString(addedIndexEntries), Toast.LENGTH_LONG)
				.show();
	}

	public void cancelFavoritesSelection(Activity activity, ListView shuffleList) {
		doCancel(activity, shuffleList);
	}

	private void doCancel(final Activity activity, final ListView shuffleList) {
		final GenericRowAdapter adapter = (GenericRowAdapter) shuffleList.getAdapter();
		adapter.setShowFavoritesSelection(false);
		adapter.setShowDurations(true);
		adapter.notifyDataSetChanged();
	}
}
