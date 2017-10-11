


package com.frozen_foo.shuffle_my_music_2

import java.io.InputStream
import java.util.List

import com.frozen_foo.shuffle_my_music_2.impl.IndexEntriesService
import com.frozen_foo.shuffle_my_music_2.impl.LocalSongsService
import com.frozen_foo.shuffle_my_music_2.impl.favorites.FavoritesCollectionService
import com.frozen_foo.shuffle_my_music_2.impl.favorites.FavoritesService

class ShuffleMyMusicService {

	IndexEntry[] randomIndexEntries(InputStream indexInputStream, int numberOfSongs) {
		new IndexEntriesService().randomIndexEntries(indexInputStream, numberOfSongs)
	}

	IndexEntry[] addFavorites(String targetDirPath, IndexEntry[] newFavorites) {
		new FavoritesService().addFavorites(targetDirPath, newFavorites)
	}

	IndexEntry[] loadFavorites(String targetDirPath) {
		new FavoritesService().loadFavorites(targetDirPath)
	}

	String resolveFavoritesFilePath(String targetDirPath) {
		new FavoritesCollectionService().resolveFavoritesFilePath(targetDirPath)
	}

	List<IndexEntry> loadFavorites(InputStream stream) {
		new FavoritesCollectionService().loadFavorites(stream)
	}

	List<IndexEntry> join(List<IndexEntry> newFavorites, List<IndexEntry> favorites) {
		new FavoritesCollectionService().join(newFavorites, newFavorites)
	}

	void writeFavorites(List<IndexEntry> favorites, OutputStream stream) {
		new FavoritesCollectionService().writeFavorites(favorites, stream)
	}

	void createSongsFile(String targetDirPath, IndexEntry[] indexEntries) {
		new LocalSongsService().createSongsFile(targetDirPath, indexEntries)
	}

	IndexEntry[] loadSongsFile(String targetDirPath) {
		return new LocalSongsService().loadSongsFile(targetDirPath)
	}
}
