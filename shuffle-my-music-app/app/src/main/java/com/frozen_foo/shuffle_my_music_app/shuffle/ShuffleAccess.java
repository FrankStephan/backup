package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.Logger;
import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.io.remote.RemoteDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;

import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Frank on 13.10.2017.
 */

public class ShuffleAccess {

	public void cleanLocalData(final Context context) throws IOException {
		new LocalDirectoryAccess().cleanLocalDir(context);
	}

	public InputStream loadIndexFromRemote(Context context) throws SettingsAccessException, IOException {
		return new RemoteDirectoryAccess().indexStream(context);
	}

	public List<IndexEntry> shuffleIndexEntries(InputStream indexStream, int numberOfSongs) {
		return new ShuffleMyMusicService().randomIndexEntries(indexStream, numberOfSongs);
	}

	public void createLocalIndex(Context context, List<IndexEntry> shuffledIndexEntries) throws IOException {
		String localSongsDirPath = localSongsDirPath(context);
		new ShuffleMyMusicService().createSongsFile(localSongsDirPath, shuffledIndexEntries);
	}

	public List<IndexEntry> getLocalIndex(Context context) {
		String localSongsDirPath = localSongsDirPath(context);
		return new ShuffleMyMusicService().loadSongsFile(localSongsDirPath);
	}

	public void copySongFromRemoteToLocal(Context context, IndexEntry indexEntry) throws SettingsAccessException,
			IOException {
		InputStream remoteSongStream = new RemoteDirectoryAccess().songStream(context, indexEntry.getPath());
		new LocalDirectoryAccess().copyToLocal(remoteSongStream, indexEntry.getFileName(), context);
	}

	public List<IndexEntry> markAsFavorites(Context context, List<IndexEntry> indexEntries) throws IOException {
		String localSongsDirPath = localSongsDirPath(context);
		return new ShuffleMyMusicService().saveFavorites(localSongsDirPath, indexEntries, false);
	}

	public List<IndexEntry> loadMarkedFavorites(Context context) {
		return new ShuffleMyMusicService().loadFavorites(localSongsDirPath(context));
	}

	public List<IndexEntry> loadLocalFavorites(Context context) {
		String           localDirPath       = localDirPath(context);
		List<IndexEntry> localFavoritesCollection       = new ShuffleMyMusicService().loadFavorites(localDirPath);
		return localFavoritesCollection;
	}

	public void saveFavoritesToLocalAnRemoteCollection(Context context) throws SettingsAccessException, IOException {
		List<IndexEntry> remoteFavoritesCollection;
		try (InputStream favoritesFileInStream = new RemoteDirectoryAccess().favoritesFileInStream(context)) {
			remoteFavoritesCollection =
					new ShuffleMyMusicService().loadFavorites(favoritesFileInStream);
		}

		if (null != remoteFavoritesCollection) {
			List<IndexEntry> markedFavorites       = loadMarkedFavorites(context);
			List<IndexEntry> resultingFavorites = new ShuffleMyMusicService().join(
					markedFavorites, remoteFavoritesCollection);

			if (CollectionUtils.isNotEmpty(resultingFavorites)) {
				try (OutputStream favoritesFileOutStream = new RemoteDirectoryAccess().favoritesFileOutStream(context)) {
					new ShuffleMyMusicService().writeFavorites(resultingFavorites, favoritesFileOutStream);
					new ShuffleMyMusicService().saveFavorites(localDirPath(context), resultingFavorites, false);
				}
			}
		}
	}

	public File[] resolveLocalSongs(Context context, List<IndexEntry> indexEntries) {
		File localSongsDir = new LocalDirectoryAccess().localSongsDir(context);
		File[] localSongs = new File[indexEntries.size()];
		for (int i = 0; i < localSongs.length; i++) {
			localSongs[i] = new File(localSongsDir, indexEntries.get(i).getFileName());
		}
		return localSongs;
	}

	@NonNull
	private String localSongsDirPath(Context context) {
		return new LocalDirectoryAccess().localSongsDir(context).getPath();
	}

	@NonNull
	private String localDirPath(final Context context) {
		return new LocalDirectoryAccess().localDir(context).getPath();
	}
}
