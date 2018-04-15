package com.frozen_foo.shuffle_my_music_app.ui.type_dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;

/**
 * Created by Frank on 16.02.2018.
 */

public class TypeDialogController {

	private NumberPicker numberPicker;
	private AlertDialog alertDialog;

	public void showDialog(Activity activity, DialogInterface.OnClickListener acceptListener) {
		createDialog(activity, acceptListener);
		configureRadioButtons(activity);
		configureNumberPicker();
	}

	private void createDialog(final Activity activity, final DialogInterface.OnClickListener acceptListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setTitle(R.string.selectListType);
		builder.setPositiveButton(R.string.accept, acceptListener);
		builder.setNegativeButton(R.string.cancel, null);

		builder.setView(R.layout.dialog_shuffle_list);

		alertDialog = builder.create();
		alertDialog.show();
	}

	private void configureRadioButtons(final Activity activity) {
		RadioButton newShuffleListRadioButton = newShuffleListRadioButton();
		newShuffleListRadioButton.setChecked(true);
		RadioButton favoritesListRadioButton = favoritesListRadioButton();
		favoritesListRadioButton.setText(secondRadioButtonText(activity));

		favoritesListRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
				numberPicker.setEnabled(!isChecked);
			}
		});
	}

	private void configureNumberPicker() {
		numberPicker = (NumberPicker) alertDialog.findViewById(R.id.numberPicker);
		numberPicker.setMinValue(1);
		numberPicker.setMaxValue(100);
		numberPicker.setValue(10);
	}

	private RadioButton newShuffleListRadioButton() {
		return (RadioButton) alertDialog.findViewById(R.id.newShuffleListRadioButton);
	}

	private RadioButton favoritesListRadioButton() {
		return (RadioButton) alertDialog.findViewById(R.id.favoritesListRadioButton);
	}

	private String secondRadioButtonText(Activity activity) {
		ShuffleAccess shuffleAccess = new ShuffleAccess();
		int numberOfLocalFavorites = shuffleAccess.loadLocalFavorites(activity).size();
		int numberOfMarkedFavorites = shuffleAccess.loadMarkedFavorites(activity).size();
		return activity.getString(R.string.typeAllFavoritesList, numberOfLocalFavorites, numberOfMarkedFavorites);
	}

	public TypeResult getTypeResult() {
		return newShuffleListRadioButton().isChecked() ? TypeResult.NEW_SHUFFLE_LIST : TypeResult.ALL_FAVORITES_LIST;
	}

	public int getNumberOfNewSongs() {
		return numberPicker.getValue();
	}



}
