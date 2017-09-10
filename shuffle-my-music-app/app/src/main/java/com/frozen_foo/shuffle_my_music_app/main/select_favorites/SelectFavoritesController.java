package com.frozen_foo.shuffle_my_music_app.main.select_favorites;

import android.app.Activity;
import android.database.DataSetObserver;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.frozen_foo.shuffle_my_music_app.main.Mode;
import com.frozen_foo.shuffle_my_music_app.main.RowModel;
import com.frozen_foo.shuffle_my_music_app.main.show_list.ShowListRowAdapter;

/**
 * Created by Frank on 24.08.2017.
 */

public class SelectFavoritesController {

	private DataSetObserver checkBoxObserver = new DataSetObserver() {
		@Override
		public void onChanged() {

		}
	};

	public void selectAddFavorites(Activity activity, ListView shuffleList, ToggleButton selectAddFavoritesButton, Mode mode) {
		switch (mode) {
			case SELECT_FAVORITES:
				startFavoritesSelection(activity, shuffleList, selectAddFavoritesButton);
				break;
			case SHOW_LIST:
				addFavorites();
				break;
		}
	}

	private void startFavoritesSelection(Activity activity, ListView shuffleList, ToggleButton selectAddFavoritesButton) {
		selectAddFavoritesButton.setEnabled(false);
		final ListAdapter          adapter              = shuffleList.getAdapter();
		RowModel[]                 rows                 = rowsFrom(adapter);
		final SelectableRowAdapter selectableRowAdapter = new SelectableRowAdapter(activity, rows);
		observeFavoritesChanges(selectableRowAdapter, selectAddFavoritesButton);
		shuffleList.setAdapter(selectableRowAdapter);
	}

	private void observeFavoritesChanges(final SelectableRowAdapter selectableRowAdapter, final ToggleButton selectAddFavoritesButton) {
		selectableRowAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				selectAddFavoritesButton.setEnabled(atLeastOneSelected(selectableRowAdapter));
			}
		});
	}

	private boolean atLeastOneSelected(SelectableRowAdapter selectableRowAdapter) {
		RowModel[] rows = rowsFrom(selectableRowAdapter);
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

	private void addFavorites() {

	}

	public void cancelFavoritesSelection(Activity activity, ListView shuffleList, ToggleButton selectAddFavoritesButton) {
		final ListAdapter adapter = shuffleList.getAdapter();
		RowModel[]        rows    = rowsFrom(adapter);
		ShowListRowAdapter showListRowAdapter = new ShowListRowAdapter(activity, rows);
		shuffleList.setAdapter(showListRowAdapter);
	}
}
