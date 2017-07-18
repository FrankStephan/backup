package com.frozen_foo.shuffle_my_music_app.shuffle_list_activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.RowModel;
import com.frozen_foo.shuffle_my_music_app.SettingsActivity;
import com.frozen_foo.shuffle_my_music_app.local.StoragePermissionService;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.ListPlayer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ShuffleListActivity extends AppCompatActivity {
	private static String SHUFFLE_MY_MUSIC_FOLDER = "_shuffle-my-music";
	private ListPlayer listPlayer;
	private File[] files;

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
				playPause(item);
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

		requestPermissionOrShowList();
	}

	private void requestPermissionOrShowList() {
		if (new StoragePermissionService().hasExternalStoragePermission(this)) {
			loadListAndPlayer();
		} else {
			new StoragePermissionService().requestExternalStorageAccess(this);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (new StoragePermissionService().hasExternalStoragePermission(this)) {
			loadListAndPlayer();
		}
	}

	private void loadListAndPlayer() {
		File shuffleMyMusicDir = new File(Environment.getExternalStorageDirectory(),
				SHUFFLE_MY_MUSIC_FOLDER);
		if (shuffleMyMusicDir.exists()) {
			loadList(shuffleMyMusicDir);
			loadPlayer();
		}
	}

	private void loadList(File shuffleMyMusicDir) {
		ListView shuffleList = (ListView) findViewById(R.id.shuffleList);

		files = shuffleMyMusicDir.listFiles();
		Arrays.sort(files);
		RowModel[] rows = new RowModel[files.length];
		for (int i = 0; i < files.length; i++) {
			rows[i] = new RowModel(files[i].getName(), files[i].getPath(), false);
		}

		RowAdapter adapter = new RowAdapter(this, rows);
		shuffleList.setAdapter(adapter);
	}

	private void loadPlayer() {
		listPlayer = new ListPlayer(getApplication(), files, new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(getApplicationContext(), "MediaPlayer Error " + what + " " + extra,
						Toast.LENGTH_LONG);
				return false;
			}
		});
	}

	public void playPause(MenuItem menuItem) {
		if (listPlayer.isPlaying()) {
			menuItem.setIcon(R.drawable.ic_play_arrow_black_24dp);
			listPlayer.pause();
		} else {
			menuItem.setIcon(R.drawable.ic_pause_black_24dp);
			listPlayer.start();
		}
	}

	@Override
	protected void onDestroy() {
		listPlayer.release();
		super.onDestroy();
	}
}
