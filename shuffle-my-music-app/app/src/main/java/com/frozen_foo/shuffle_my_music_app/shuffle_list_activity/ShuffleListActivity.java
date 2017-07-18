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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.RowModel;
import com.frozen_foo.shuffle_my_music_app.SettingsActivity;
import com.frozen_foo.shuffle_my_music_app.local.StoragePermissionService;

import java.io.File;
import java.io.IOException;

public class ShuffleListActivity extends AppCompatActivity {
	private static String SHUFFLE_MY_MUSIC_FOLDER = "_shuffle-my-music";
	private MediaPlayer mediaPlayer;
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
			list();
		} else {
			new StoragePermissionService().requestExternalStorageAccess(this);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		if (new StoragePermissionService().hasExternalStoragePermission(this)) {
			list();
		}
	}

	private void list() {
		File shuffleMyMusicDir = new File(Environment.getExternalStorageDirectory(),
				SHUFFLE_MY_MUSIC_FOLDER);
		if (shuffleMyMusicDir.exists()) {

			ListView shuffleList = (ListView) findViewById(R.id.shuffleList);

			files = shuffleMyMusicDir.listFiles();
			RowModel[] rows = new RowModel[files.length];
			for (int i = 0; i < files.length; i++) {
				rows[i] = new RowModel(files[i].getName(), files[i].getPath(), false);
			}

			RowAdapter adapter = new RowAdapter(this, rows);
			shuffleList.setAdapter(adapter);

		}
	}

	public void playPause(MenuItem menuItem) {
		initMediaPlayerIfNeeded();

		if (mediaPlayer.isPlaying()) {
			menuItem.setIcon(R.drawable.ic_play_arrow_black_24dp);
			mediaPlayer.pause();
		} else {
			menuItem.setIcon(R.drawable.ic_pause_black_24dp);
			mediaPlayer.start();
		}
	}



	private void initMediaPlayerIfNeeded() {
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
				mediaPlayer.setDataSource(getApplicationContext(), Uri.fromFile(files[0].getParentFile()));
				mediaPlayer.prepare();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
				e.printStackTrace();
			}
		}
	}

	private void startMediaPlayer(File[] files) {


		mediaPlayer.start();

		// AudioManager.ROUTE_BLUETOOTH_SCO;

	}


}
