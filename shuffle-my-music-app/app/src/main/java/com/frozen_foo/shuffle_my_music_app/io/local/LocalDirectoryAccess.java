package com.frozen_foo.shuffle_my_music_app.io.local;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Frank on 01.08.2017.
 */

public class LocalDirectoryAccess {

	private static String SHUFFLE_MY_MUSIC_SONGS_DIR = "songs";

	public void cleanLocalDir(final Context context) throws IOException {
		File shuffleMyMusicDir = localSongsDir(context);
		if (shuffleMyMusicDir.exists()) {
			FileUtils.cleanDirectory(shuffleMyMusicDir);
		}
	}

	public void copyToLocal(InputStream source, String fileName, final Context context) throws IOException {
		File shuffleMyMusicDir = localSongsDir(context);
		File localFile = new File(shuffleMyMusicDir, fileName);
		FileUtils.copyInputStreamToFile(source, localFile);
	}

	@NonNull
	public File localDir(Context context) {
		return new File(Environment.getExternalStorageDirectory(), new SettingsAccess().readLocalDir(context));
	}

	@NonNull
	public File localSongsDir(final Context context) {
		return new File(localDir(context), SHUFFLE_MY_MUSIC_SONGS_DIR);
	}
}
