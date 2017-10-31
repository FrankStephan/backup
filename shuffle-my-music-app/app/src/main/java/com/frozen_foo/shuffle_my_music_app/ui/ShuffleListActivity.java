package com.frozen_foo.shuffle_my_music_app.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.ListPlayerControllerListener;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.CreateListController;
import com.frozen_foo.shuffle_my_music_app.ui.select_favorites.SelectFavoritesController;
import com.frozen_foo.shuffle_my_music_app.ui.show_list.ShowListController;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.ListPlayerController;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionsAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsActivity;

import static com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest.READ_EXTERNAL_STORAGE_REQUEST;

public class ShuffleListActivity extends AppCompatActivity {

	public static final int NUMBER_OF_SONGS = 10;
	private ListPlayerController listPlayerController;

	private ListView list() {
		return (ListView) findViewById(R.id.shuffleList);
	}

	private Button button1() {
		return (Button) findViewById(R.id.createShuffleListButton);
	}

	private ToggleButton button2() {
		return (ToggleButton) findViewById(R.id.selectAddFavoritesButton);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menubar, menu);
		Object o = menu.findItem(R.id.play_pause);
		listPlayerController.initPlayer(getApplicationContext(), list(), menu.findItem(R.id.play_pause), markPlayingSongListener());
		return true;
	}

	private ListPlayerControllerListener markPlayingSongListener() {
		return new ListPlayerControllerListener() {

			@Override
			public void playingSongChanged(final int index) {
				new ShowListController().markAsPlayingSong(list(), index);
			}
		};
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				openSettings();
				return true;
			case R.id.play_pause:
				listPlayerController.playPause();
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shuffle_list);
		listPlayerController = new ListPlayerController();
		requestExternalStorageAccessOrShowList();
	}

	private void requestExternalStorageAccessOrShowList() {
		if (new PermissionsAccess().hasPermission(this, READ_EXTERNAL_STORAGE_REQUEST)) {
			loadList();
		} else {
			new PermissionsAccess().requestPermission(this, READ_EXTERNAL_STORAGE_REQUEST);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions, @NonNull int[] grantResults) {
		PermissionRequest permissionRequest = new PermissionsAccess().forRequestCode(requestCode);
		if (new PermissionsAccess().hasPermission(this, permissionRequest)) {
			switch (permissionRequest) {
				case READ_EXTERNAL_STORAGE_REQUEST:
					loadList();
					break;
				case INTERNET_REQUEST:
					createShuffleList();
					break;
				default:
					break;
			}
		}
	}
	private void loadList() {
		new ShowListController().loadAndInflateList(this, list());
	}

	@Override
	protected void onDestroy() {
		listPlayerController.release();
		super.onDestroy();
	}

	private Mode mode() {
		ToggleButton selectAddFavoritesButton = button2();
		return selectAddFavoritesButton.isChecked() ? Mode.SELECT_FAVORITES : Mode.SHOW_LIST;
	}

	public void pressButton1(View view) {
		Mode mode = mode();
		switch (mode) {
			case SHOW_LIST:
				if (new PermissionsAccess().hasPermission(this, PermissionRequest.INTERNET_REQUEST,
						PermissionRequest.WRITE_EXTERNAL_STORAGE_REQUEST)) {
					createShuffleList();
				} else {
					new PermissionsAccess().requestPermission(this, PermissionRequest.INTERNET_REQUEST,
							PermissionRequest.WRITE_EXTERNAL_STORAGE_REQUEST);
				}
				break;
			case SELECT_FAVORITES:
				button1().setText(R.string.createShuffledList);
				button1().setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
				button2().setChecked(false);
				button2().setEnabled(true);
				cancelFavoritesSelection();
				break;
		}

	}

/*	private void confirmCreateShuffleList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setMessage(R.string.confirmCreateShuffleList);
		--> Thread wird nicht angehalten
		builder.show();

	}*/



	public void pressButton2(View view) {
		final ListView     shuffleList = list();
		final ToggleButton button2     = button2();
		Mode               mode        = mode();
		switch (mode) {
			case SHOW_LIST:
				button1().setText(R.string.createShuffledList);
				button1().setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
				addSelectedFavorites(shuffleList);
				break;
			case SELECT_FAVORITES:
				button1().setText(R.string.cancel);
				button1().setBackgroundTintList(ColorStateList.valueOf(Color.RED));
				selectFavorites(shuffleList, button2);
				break;
		}
	}

	private void createShuffleList() {
		listPlayerController.release();
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		new CreateListController().createShuffleList(getApplicationContext(), this, progressBar, NUMBER_OF_SONGS, false);
	}

	private void selectFavorites(final ListView shuffleList, final ToggleButton button2) {
		final SelectFavoritesController selectFavoritesController = new SelectFavoritesController();
		selectFavoritesController.selectFavorites(this, shuffleList, new DataSetObserver() {
			@Override
			public void onChanged() {
				button2.setEnabled(selectFavoritesController.atLeastOneSelected(shuffleList));
			}
		});
	}

	private void addSelectedFavorites(final ListView shuffleList) {
		new SelectFavoritesController().addFavorites(this, shuffleList);
	}

	private void cancelFavoritesSelection() {
		new SelectFavoritesController().cancelFavoritesSelection(this, list());
	}

	public void reload(View view) {
		listPlayerController.release();
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		new CreateListController().createShuffleList(getApplicationContext(), this, progressBar, NUMBER_OF_SONGS, true);
	}
}
