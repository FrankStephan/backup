package com.frozen_foo.shuffle_my_music_app.main;

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

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.main.create_list.CreateListController;
import com.frozen_foo.shuffle_my_music_app.main.select_favorites.SelectFavoritesController;
import com.frozen_foo.shuffle_my_music_app.main.show_list.ShowListController;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.ListPlayerController;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionsAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsActivity;

import java.io.File;

import static com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest.READ_EXTERNAL_STORAGE_REQUEST;

public class ShuffleListActivity extends AppCompatActivity {

	public static final int NUMBER_OF_SONGS = 10;
	private static String SHUFFLE_MY_MUSIC_FOLDER = "_shuffle-my-music";
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				openSettings();
				return true;
			case R.id.play_pause:
				listPlayerController.playPause(item);
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
		listPlayerController = new ListPlayerController(getApplication(), getApplicationContext());
		requestExternalStorageAccessOrShowList();
	}

	private void requestExternalStorageAccessOrShowList() {
		if (new PermissionsAccess().hasPermission(this, READ_EXTERNAL_STORAGE_REQUEST)) {
			loadListAndPlayer();
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
					loadListAndPlayer();
					break;
				case INTERNET_REQUEST:
					createShuffleList();
					break;
				default:
					break;
			}
		}
	}

	private void loadListAndPlayer() {
		IndexEntry[] songs = new ShowListController().loadAndInflateList(this, list());
		listPlayerController.loadPlayer();
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
				new SelectFavoritesController().cancelFavoritesSelection(this, list());
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

	private void createShuffleList() {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		new CreateListController().createShuffleList(getApplicationContext(), this, progressBar, NUMBER_OF_SONGS);
	}

	public void pressButton2(View view) {
		final ListView     shuffleList = list();
		final ToggleButton button2     = button2();
		Mode               mode        = mode();
		switch (mode) {
			case SHOW_LIST:
				button1().setText(R.string.createShuffledList);
				button1().setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
				new SelectFavoritesController().addFavorites(this, shuffleList);
				break;
			case SELECT_FAVORITES:
				button1().setText(R.string.cancel);
				button1().setBackgroundTintList(ColorStateList.valueOf(Color.RED));
				final SelectFavoritesController selectFavoritesController = new SelectFavoritesController();
				selectFavoritesController.selectFavorites(this, shuffleList, new DataSetObserver() {
					@Override
					public void onChanged() {
						button2.setEnabled(selectFavoritesController.atLeastOneSelected(shuffleList));
					}
				});
				break;
		}
	}
}
