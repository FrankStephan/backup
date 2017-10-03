package com.frozen_foo.shuffle_my_music_2

import com.frozen_foo.shuffle_my_music_2.impl.FavoritesService
import com.frozen_foo.shuffle_my_music_2.impl.IndexEntriesService
import com.frozen_foo.shuffle_my_music_2.impl.LocalSongsService

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

	void createSongsFile(String targetDirPath, IndexEntry[] indexEntries) {
		new LocalSongsService().createSongsFile(targetDirPath, indexEntries)
	}

	IndexEntry[] loadSongsFile(String targetDirPath) {
		return new LocalSongsService().loadSongsFile(targetDirPath)
	}
}
