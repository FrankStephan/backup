package com.frozen_foo.shuffle_my_music_app.ui.number_picker;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.NumberPicker;

import com.frozen_foo.shuffle_my_music_app.R;

/**
 * Created by Frank on 16.02.2018.
 */

public class NumberPickerController {

	private NumberPicker numberPicker;

	public void showDialog(Activity activity, DialogInterface.OnClickListener acceptListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		numberPicker = new NumberPicker(activity);

		builder.setTitle(R.string.confirmCreateShuffleList);
		builder.setPositiveButton(R.string.accept, acceptListener);
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				// Do nothing
			}
		});

		numberPicker.setMinValue(1);
		numberPicker.setMaxValue(100);
		numberPicker.setValue(10);

		builder.setView(numberPicker);
		builder.create().show();
	}

	public int getNumber() {
		return numberPicker.getValue();
	}
}
