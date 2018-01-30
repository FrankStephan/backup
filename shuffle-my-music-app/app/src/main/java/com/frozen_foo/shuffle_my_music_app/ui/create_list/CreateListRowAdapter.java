package com.frozen_foo.shuffle_my_music_app.ui.create_list;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;

/**
 * Created by Frank on 05.08.2017.
 */

public class CreateListRowAdapter extends ArrayAdapter<RowModel> {

	public CreateListRowAdapter(@NonNull Context context, RowModel[] rows) {
		super(context, R.layout.create_list_row, rows);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Context        context  = super.getContext();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		convertView = inflater.inflate(R.layout.create_list_row, parent, false);
		RowModel rowModel     = getItem(position);
		TextView songNameText = (TextView) convertView.findViewById(R.id.songNameText);
		songNameText.setText(rowModel.getLabel());
		ProgressBar copyProgress = (ProgressBar) convertView.findViewById(R.id.copyProgress);
		if (rowModel.isCopying()) {
			copyProgress.setVisibility(View.VISIBLE);
		} else {
			copyProgress.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
}
