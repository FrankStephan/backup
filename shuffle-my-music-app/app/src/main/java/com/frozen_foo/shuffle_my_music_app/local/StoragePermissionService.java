package com.frozen_foo.shuffle_my_music_app.local;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Frank on 16.07.2017.
 */

public class StoragePermissionService {

	private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST = 0;

	public void requestExternalStorageAccess(Activity activity) {
		if (ContextCompat.checkSelfPermission(activity,
				Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(activity,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					EXTERNAL_STORAGE_PERMISSION_REQUEST);
		}
	}

	public boolean checkPermissionResult(int requestCode, String permissions[], int[]
			grantResults) {
		switch (requestCode) {
			case EXTERNAL_STORAGE_PERMISSION_REQUEST:
				return grantResults.length > 0 && grantResults[0] == PackageManager
						.PERMISSION_GRANTED;
			default: return false;
		}
	}
}
