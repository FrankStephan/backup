package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;
import android.support.annotation.NonNull;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.io.remote.RemoteDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsAccessException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		String localDirPath = localSongsDirPath(context);
		new ShuffleMyMusicService().createSongsFile(localDirPath, shuffledIndexEntries);
	}

	public List<IndexEntry> getLocalIndex(Context context) throws IOException {
		String localDirPath = localSongsDirPath(context);
		return new ShuffleMyMusicService().loadSongsFile(localDirPath);
	}

	public void copySongFromRemoteToLocal(Context context, IndexEntry indexEntry) throws SettingsAccessException,
			IOException {
		InputStream remoteSongStream = new RemoteDirectoryAccess().songStream(context, indexEntry.getPath());
		new LocalDirectoryAccess().copyToLocal(remoteSongStream, indexEntry.getFileName(), context);
	}

	public List<IndexEntry> markAsFavorites(Context context, List<IndexEntry> indexEntries) throws IOException {
		String localDirPath = localSongsDirPath(context);
		return new ShuffleMyMusicService().saveFavorites(localDirPath, indexEntries, false);
	}

	public List<IndexEntry> loadMarkedFavorites(Context context) throws IOException {
		return new ShuffleMyMusicService().loadFavorites(localSongsDirPath(context));
	}

	public void addFavoritesToLocalCollection(Context context) throws IOException {
		String           localDirPath       = new LocalDirectoryAccess().localDir(context).getPath();
		List<IndexEntry> favorites          = new ShuffleMyMusicService().loadFavorites(localDirPath);
		List<IndexEntry> newFavorites       = loadMarkedFavorites(context);
		List<IndexEntry> resultingFavorites = new ShuffleMyMusicService().join(newFavorites, favorites);
		new ShuffleMyMusicService().saveFavorites(localDirPath, resultingFavorites, true);
	}

	public void backupFavoritesCollectionToRemote(Context context) throws SettingsAccessException, IOException {
		String           localDirPath             = new LocalDirectoryAccess().localDir(context).getPath();
		List<IndexEntry> localFavoritesCollection = new ShuffleMyMusicService().loadFavorites(localDirPath);

		List<IndexEntry> resultingFavorites;
		try (InputStream favoritesFileInStream = new RemoteDirectoryAccess().favoritesFileInStream(context)) {
			List<IndexEntry> remoteFavoritesCollection =
					new ShuffleMyMusicService().loadFavorites(favoritesFileInStream);
			resultingFavorites = new ShuffleMyMusicService().join(localFavoritesCollection, remoteFavoritesCollection);
		}

		if (null != resultingFavorites) {
			try (OutputStream favoritesFileOutStream = new RemoteDirectoryAccess().favoritesFileOutStream(context)) {
				new ShuffleMyMusicService().writeFavorites(resultingFavorites, favoritesFileOutStream);
			}
		}
	}

	public File[] resolveLocalSongs(Context context, List<IndexEntry> indexEntries) throws IOException {
		File localSongsDir = new LocalDirectoryAccess().localSongsDir(context);
		File[] localSongs = new File[indexEntries.size()];
		for (int i = 0; i < localSongs.length; i++) {
			localSongs[i] = new File(localSongsDir, indexEntries.get(i).getFileName());
		}
		return localSongs;
	}

	@NonNull
	private String localSongsDirPath(Context context) throws IOException {
		return new LocalDirectoryAccess().localSongsDir(context).getPath();
	}
}
