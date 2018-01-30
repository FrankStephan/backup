package com.frozen_foo.shuffle_my_music_app.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frozen_foo.shuffle_my_music_app.R;

/**
 * Created by Frank on 24.01.2018.
 */

public class GenericRowAdapter extends ArrayAdapter<RowModel> {

	private boolean showFavoritesSelection;

	public GenericRowAdapter(@NonNull Context context, RowModel[] rows) {
		super(context, R.layout.generic_list_row, rows);

	}

	public boolean isShowFavoritesSelection() {
		return showFavoritesSelection;
	}

	public void setShowFavoritesSelection(final boolean showFavoritesSelection) {
		this.showFavoritesSelection = showFavoritesSelection;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Context        context  = super.getContext();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		convertView = inflater.inflate(R.layout.generic_list_row, parent, false);
		TextView name     = (TextView) convertView.findViewById(R.id.songNameText);
		RowModel rowModel = getItem(position);
		name.setText(rowModel.getLabel());

		setDuration(convertView, rowModel);
		setPlayIcon(convertView, rowModel);
		setProgress(convertView, rowModel);
		setAndBindCheckbox(convertView, rowModel);

		return convertView;
	}

	private void setProgress(final @Nullable View convertView, final RowModel rowModel) {
		ProgressBar copyProgress = (ProgressBar) convertView.findViewById(R.id.copyProgress);
		if (rowModel.isCopying()) {
			copyProgress.setVisibility(View.VISIBLE);
		} else {
			copyProgress.setVisibility(View.GONE);
		}
	}

	private void setPlayIcon(final @Nullable View convertView, final RowModel rowModel) {
		ImageView playingIcon = (ImageView) convertView.findViewById(R.id.playingIcon);
		playingIcon.setVisibility(rowModel.isPlaying() ? View.VISIBLE : View.GONE);
	}

	private void setDuration(final @Nullable View convertView, final RowModel rowModel) {
		TextView duration = (TextView) convertView.findViewById(R.id.durationText);
		duration.setText(rowModel.getDuration());
	}

	private void setAndBindCheckbox(final @Nullable View convertView, final RowModel rowModel) {
		final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
		if (showFavoritesSelection) {
			cb.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					rowModel.setFavorite(cb.isChecked());
					notifyDataSetChanged();
				}
			});
			cb.setChecked(rowModel.isFavorite());
			cb.setVisibility(View.VISIBLE);
		} else {
			cb.setVisibility(View.GONE);
		}
	}
}
