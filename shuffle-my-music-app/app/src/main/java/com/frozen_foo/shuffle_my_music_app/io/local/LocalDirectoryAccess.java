package com.frozen_foo.shuffle_my_music_app.io.local;

import android.os.Environment;
import android.support.annotation.NonNull;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Frank on 01.08.2017.
 */

public class LocalDirectoryAccess {

	private static String SHUFFLE_MY_MUSIC_DIR = "_shuffle-my-music";
	private static String SHUFFLE_MY_MUSIC_SONGS_DIR = "songs";


	public void cleanLocalDir() throws IOException {
		File shuffleMyMusicDir = localSongsDir();
		if (shuffleMyMusicDir.exists()) {
			FileUtils.cleanDirectory(shuffleMyMusicDir);
		}
	}

	public void copyToLocal(InputStream source, String fileName) throws IOException {
		File shuffleMyMusicDir = localSongsDir();
		File localFile = new File(shuffleMyMusicDir, fileName);
		FileUtils.copyInputStreamToFile(source, localFile);
	}

	@NonNull
	public File localDir() {
		return new File(Environment.getExternalStorageDirectory(), SHUFFLE_MY_MUSIC_DIR);
	}

	@NonNull
	public File localSongsDir() {
		return new File(localDir(), SHUFFLE_MY_MUSIC_SONGS_DIR);
	}


}
