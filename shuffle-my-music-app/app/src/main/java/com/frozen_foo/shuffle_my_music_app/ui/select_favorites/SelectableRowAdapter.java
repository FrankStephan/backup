package com.frozen_foo.shuffle_my_music_app.ui.select_favorites;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Frank on 04.07.2017.
 */

public class SelectableRowAdapter extends ArrayAdapter<RowModel> {

	private final List<DataSetObserver> dataSetObservers = new LinkedList<>();

	public SelectableRowAdapter(@NonNull Context context, RowModel[] resource) {
		super(context, R.layout.select_favorite_row, resource);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Context        context  = super.getContext();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		convertView = inflater.inflate(R.layout.select_favorite_row, parent, false);
		RowModel rowModel = getItem(position);

		TextView name     = (TextView) convertView.findViewById(R.id.songNameText);
		name.setText(rowModel.getLabel());

		CheckBox cb       = (CheckBox) convertView.findViewById(R.id.checkBox1);

		bind(cb, rowModel);
		return convertView;
	}

	private void bind(final CheckBox view, final RowModel model) {
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				model.setFavorite(view.isChecked());
				notifyDataSetChanged();
			}
		});
		view.setChecked(model.isFavorite());
	}

	@Override
	public void registerDataSetObserver(final DataSetObserver observer) {
		super.registerDataSetObserver(observer);
		dataSetObservers.add(observer);
	}

	@Override
	public void unregisterDataSetObserver(final DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
		dataSetObservers.remove(observer);
	}

	public void release() {
		for (DataSetObserver dataSetObserver : dataSetObservers) {
			unregisterDataSetObserver(dataSetObserver);
		}
	}
}
