package com.frozen_foo.shuffle_my_music_app.permission;

import android.Manifest;

/**
 * Created by Frank on 19.07.2017.
 */

public enum PermissionRequest {

	EXTERNAL_STORAGE_PERMISSION_REQUEST(Manifest.permission.READ_EXTERNAL_STORAGE),
	INTERNET_PERMISSION_REQUEST(Manifest.permission.INTERNET);

	public final String permission;

	private PermissionRequest(String permission) {
		this.permission = permission;
	}
}
