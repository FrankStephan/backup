package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.frozen_foo.shuffle_my_music_app.permission.PermissionsAccess;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Frank on 01.08.2017.
 */

public class LocalDirectoryAccess {

	private static String SHUFFLE_MY_MUSIC_FOLDER = "_shuffle-my-music";

	public void copyToLocal(InputStream remoteSongStream, String fileName) throws IOException {
		File shuffleMyMusicDir = localDir();
		FileUtils.cleanDirectory(shuffleMyMusicDir);
		//FileUtils.forceDelete(shuffleMyMusicDir);
		File localSong = new File(shuffleMyMusicDir, fileName);
		FileUtils.copyInputStreamToFile(remoteSongStream, localSong);
	}

	File[] songs() {
		return localDir().listFiles();
	}

	@NonNull
	private File localDir() {
		return new File(Environment.getExternalStorageDirectory(), SHUFFLE_MY_MUSIC_FOLDER);
	}
}
