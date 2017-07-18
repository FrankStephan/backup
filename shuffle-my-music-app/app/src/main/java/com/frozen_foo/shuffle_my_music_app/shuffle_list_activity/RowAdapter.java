package com.frozen_foo.shuffle_my_music_app.shuffle_list_activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.RowModel;

/**
 * Created by Frank on 16.07.2017.
 */

public class RowAdapter extends ArrayAdapter<RowModel> {

	public RowAdapter(@NonNull Context context, RowModel[] rows) {
		super(context, R.layout.row, rows);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Context context = super.getContext();
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		convertView = inflater.inflate(R.layout.row, parent, false);
		TextView name = (TextView) convertView.findViewById(R.id.textView1);


	name.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			return true;
		}
	});
		RowModel rowModel = (RowModel)getItem(position);
		name.setText(rowModel.getLabel());
		return convertView;
	}


}
