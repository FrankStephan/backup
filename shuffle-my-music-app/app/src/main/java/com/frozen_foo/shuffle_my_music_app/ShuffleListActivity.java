package com.frozen_foo.shuffle_my_music_app;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.local.LocalDirectoryTask;
import com.frozen_foo.shuffle_my_music_app.local.StoragePermissionService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ShuffleListActivity extends AppCompatActivity {
	private static String SHUFFLE_MY_MUSIC_FOLDER = "_shuffle-my-music";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shuffle_list);

		ListView shuffleList = (ListView) findViewById(R.id.shuffleList);

		new StoragePermissionService().requestExternalStorageAccess(this);
	}

	private void list() {
		File shuffleMyMusicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (shuffleMyMusicDir.exists()) {
			String[] list;
			list = shuffleMyMusicDir.list();
			Toast.makeText(getApplicationContext(), Arrays.toString(shuffleMyMusicDir.list()), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (new StoragePermissionService().checkPermissionResult(requestCode, permissions, grantResults)) {
			list();
		}
	}
}
