package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.SettingsActivity;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.ListPlayerController;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionsAccess;

import java.io.File;

import static com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest.READ_EXTERNAL_STORAGE_REQUEST;

public class ShuffleListActivity extends AppCompatActivity {

	public static final int NUMBER_OF_SONGS = 10;
	private static String SHUFFLE_MY_MUSIC_FOLDER = "_shuffle-my-music";
	private ListPlayerController listPlayerController;
	private ShuffleListController shuffleListController;
	private File[] songs;

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
		shuffleListController = new ShuffleListController(this, getApplicationContext());
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
					shuffleListController.createShuffleList(NUMBER_OF_SONGS);
					break;
				default:
					break;
			}
		}
	}

	private void loadListAndPlayer() {
		File shuffleMyMusicDir = new File(Environment.getExternalStorageDirectory(), SHUFFLE_MY_MUSIC_FOLDER);
		if (shuffleMyMusicDir.exists()) {
			songs = shuffleListController.loadAndInflateList(shuffleMyMusicDir);
			listPlayerController.loadPlayer(songs);
		}
	}

	@Override
	protected void onDestroy() {
		listPlayerController.release();
		super.onDestroy();
	}

	public void createShuffleList(View view) {
		if (new PermissionsAccess().hasPermission(this, PermissionRequest.INTERNET_REQUEST,
				PermissionRequest.WRITE_EXTERNAL_STORAGE_REQUEST)) {
			shuffleListController.createShuffleList(NUMBER_OF_SONGS);
		} else {
			new PermissionsAccess().requestPermission(this, PermissionRequest.INTERNET_REQUEST,
					PermissionRequest.WRITE_EXTERNAL_STORAGE_REQUEST);
		}
	}
}
