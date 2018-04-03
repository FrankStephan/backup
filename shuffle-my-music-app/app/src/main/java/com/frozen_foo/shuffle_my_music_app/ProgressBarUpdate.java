package com.frozen_foo.shuffle_my_music_app;

import android.app.Activity;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.frozen_foo.shuffle_my_music_app.ui.GenericRowAdapter;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.ListCreationListener;

/**
 * Created by Frank on 23.03.2018.
 */

public class ProgressBarUpdate extends ProgressUIUpdate {

	private final Activity activity;
	private final ProgressBar progressBar;
	private final ListCreationListener listCreationListener;
	private final boolean forceReloadRows;
	private final ListView shuffleList;

	public ProgressBarUpdate(final Activity activity, final ListView shuffleList, final ProgressBar progressBar,
								 final ListCreationListener listCreationListener, final boolean forceFillRows) {
		this.activity = activity;
		this.progressBar = progressBar;
		this.listCreationListener = listCreationListener;
		this.forceReloadRows = forceFillRows;
		this.shuffleList = shuffleList;
	}

	@Override
	protected void defineMax(final int max) {
		progressBar.setMax(max);
	}

	@Override
	protected void update(final int progressIndex) {
		progressBar.setProgress(progressIndex);

	}

	@Override
	protected void showText(final String text) {
		showPreparation(activity, text);
	}

	private GenericRowAdapter adapter() {
		return (GenericRowAdapter) shuffleList.getAdapter();
	}

	private void showPreparation(Activity activity, String text) {
		adapter().clear();
		adapter().add(new RowModel(text, null, false));
		adapter().notifyDataSetChanged();
	}

	@Override
	protected void finish() {

	}
}
