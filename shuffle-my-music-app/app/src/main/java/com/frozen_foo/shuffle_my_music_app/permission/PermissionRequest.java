package com.frozen_foo.shuffle_my_music_app.permission;

import android.Manifest;

/**
 * Created by Frank on 19.07.2017.
 */

public enum PermissionRequest {

	READ_EXTERNAL_STORAGE_REQUEST(Manifest.permission.READ_EXTERNAL_STORAGE),
	INTERNET_REQUEST(Manifest.permission.INTERNET),
	WRITE_EXTERNAL_STORAGE_REQUEST(Manifest.permission.WRITE_EXTERNAL_STORAGE);

	public final String permission;

	PermissionRequest(String permission) {
		this.permission = permission;
	}
}
