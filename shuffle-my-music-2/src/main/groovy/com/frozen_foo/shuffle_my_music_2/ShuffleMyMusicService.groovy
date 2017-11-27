package com.frozen_foo.shuffle_my_music_2

import com.frozen_foo.shuffle_my_music_2.impl.IndexEntriesService
import com.frozen_foo.shuffle_my_music_2.impl.LocalSongsService
import com.frozen_foo.shuffle_my_music_2.impl.favorites.FavoritesCollectionService
import com.frozen_foo.shuffle_my_music_2.impl.favorites.FavoritesService

class ShuffleMyMusicService {

	List<IndexEntry> randomIndexEntries(InputStream indexFileInputStream, int numberOfSongs) {
		new IndexEntriesService().randomIndexEntries(indexFileInputStream, numberOfSongs)
	}

	List<IndexEntry> saveFavorites(String targetDirPath, List<IndexEntry> newFavorites, boolean append) {
		new FavoritesService().saveFavorites(targetDirPath, newFavorites, append)
	}

	List<IndexEntry> loadFavorites(String targetDirPath) {
		new FavoritesService().loadFavorites(targetDirPath)
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

	void createSongsFile(String targetDirPath, List<IndexEntry> indexEntries) {
		new LocalSongsService().createSongsFile(targetDirPath, indexEntries)
	}

	List<IndexEntry> loadSongsFile(String targetDirPath) {
		return new LocalSongsService().loadSongsFile(targetDirPath)
	}
}
