package com.frozen_foo.shuffle_my_music_app.shuffle_list_activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_2.Check;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.RowModel;
import com.frozen_foo.shuffle_my_music_app.SettingsActivity;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.ListPlayer;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionService;
import com.frozen_foo.shuffle_my_music_app.smb.RemoteDirectoryTask;

import java.io.File;
import java.util.Arrays;

import static com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest
		.EXTERNAL_STORAGE_PERMISSION_REQUEST;

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

		requestExternalStorageAccessOrShowList();
	}

	private void requestExternalStorageAccessOrShowList() {
		if (new PermissionService().hasPermission(this, EXTERNAL_STORAGE_PERMISSION_REQUEST)) {
			loadListAndPlayer();
		} else {
			new PermissionService().requestPermission(this, EXTERNAL_STORAGE_PERMISSION_REQUEST);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		PermissionRequest permissionRequest = new PermissionService().forRequestCode(requestCode);
		if (new PermissionService().hasPermission(this, permissionRequest)) {
			switch (permissionRequest) {
				case EXTERNAL_STORAGE_PERMISSION_REQUEST:
					loadListAndPlayer();
					break;
				case INTERNET_PERMISSION_REQUEST:
					createShuffleList();
					break;
				default:
					break;
			}
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

	public void createShuffleList(View view) {
		String s = new Check().doSomething();

		if (new PermissionService().hasPermission(this, PermissionRequest.INTERNET_PERMISSION_REQUEST)) {
			createShuffleList();
		} else {
			new PermissionService().requestPermission(this, PermissionRequest.INTERNET_PERMISSION_REQUEST);
		}
	}

	private void createShuffleList() {
		new RemoteDirectoryTask().execute(getApplicationContext());
	}

}
