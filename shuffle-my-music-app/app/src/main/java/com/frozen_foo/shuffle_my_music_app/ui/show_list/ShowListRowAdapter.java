package com.frozen_foo.shuffle_my_music_app.ui.show_list;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.ui.RowModel;

/**
 * Created by Frank on 16.07.2017.
 */

public class ShowListRowAdapter extends ArrayAdapter<RowModel> {

	public ShowListRowAdapter(@NonNull Context context, RowModel[] rows) {
		super(context, R.layout.show_list_row, rows);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Context        context  = super.getContext();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		convertView = inflater.inflate(R.layout.show_list_row, parent, false);
		TextView name     = (TextView) convertView.findViewById(R.id.songNameText);
		RowModel rowModel = getItem(position);
		name.setText(rowModel.getLabel());

		ImageView playingIcon = (ImageView) convertView.findViewById(R.id.playingIcon);
		playingIcon.setVisibility(rowModel.isPlaying() ? View.VISIBLE : View.INVISIBLE);

		return convertView;
	}


}
