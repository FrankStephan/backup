package com.frozen_foo.shuffle_my_music_app.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Frank on 19.07.2017.
 */

public class PermissionService {

	public void requestPermission(Activity activity, PermissionRequest permissionRequest) {
		ActivityCompat.requestPermissions(activity,
				new String[]{permissionRequest.permission},
				permissionRequest.ordinal());
	}

	public boolean hasPermission(Activity activity, PermissionRequest permissionRequest) {
		return ContextCompat.checkSelfPermission(activity,
				permissionRequest.permission) == PackageManager.PERMISSION_GRANTED;
	}

	public PermissionRequest forRequestCode(int requestCode) {
		return PermissionRequest.values()[requestCode];
	}
}
