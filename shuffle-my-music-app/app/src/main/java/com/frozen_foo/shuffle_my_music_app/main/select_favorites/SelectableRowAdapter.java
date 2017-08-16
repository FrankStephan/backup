package com.frozen_foo.shuffle_my_music_app.main.select_favorites;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.main.RowModel;

/**
 * Created by Frank on 04.07.2017.
 */

public class SelectableRowAdapter extends ArrayAdapter<RowModel> {

	public SelectableRowAdapter(@NonNull Context context, RowModel[] resource) {
		super(context, R.layout.selectable_row, resource);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Context        context  = super.getContext();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		convertView = inflater.inflate(R.layout.selectable_row, parent, false);
		TextView name     = (TextView) convertView.findViewById(R.id.songNameText);
		CheckBox cb       = (CheckBox) convertView.findViewById(R.id.checkBox1);
		RowModel rowModel = getItem(position);
		name.setText(rowModel.getLabel());
		if (rowModel.isFavorite()) {
			cb.setChecked(true);
		} else {
			cb.setChecked(false);
		}
		return convertView;
	}
}
