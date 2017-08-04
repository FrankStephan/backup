package com.frozen_foo.shuffle_my_music_app.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Frank on 19.07.2017.
 */

public class PermissionsAccess {

	public void requestPermission(Activity activity, PermissionRequest... permissionRequests) {
		List<PermissionRequest> permissionRequestList = Arrays.asList(permissionRequests);
		Collection<String> permissionList = CollectionUtils.collect(permissionRequestList, new Transformer() {
			@Override
			public Object transform(Object input) {
				return ((PermissionRequest) input).permission;
			}
		});
		String[] permissions = permissionList.toArray(new String[permissionList.size()]);

		int ordinal = permissionRequests[0].ordinal();

		ActivityCompat.requestPermissions(activity, permissions, ordinal);
	}


	public boolean hasPermission(Activity activity, PermissionRequest... permissionRequests) {
		for (PermissionRequest permissionRequest : permissionRequests) {
			if (ContextCompat.checkSelfPermission(activity, permissionRequest.permission)
					!= PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	public void v(String s) {

	}

	public PermissionRequest forRequestCode(int requestCode) {
		return PermissionRequest.values()[requestCode];
	}
}
