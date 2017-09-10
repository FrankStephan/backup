package com.frozen_foo.shuffle_my_music_2

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


class FavoritesService {


	IndexEntry[] addFavorites(String targetDirPath, IndexEntry[] newFavorites) {
		Path favoritesFile = createFavoritesFileIfNecessary(targetDirPath)

		return null
	}

	private Path createFavoritesFileIfNecessary(String targetDirPath) {
		Path targetDir = Paths.get(targetDirPath).toAbsolutePath()
		Files.createDirectories(targetDir)
		Path favoritesFile = targetDir.resolve('favorites.xml')
		if (!Files.exists(favoritesFile)) {
			Files.createFile(favoritesFile)
		}
		return favoritesFile
	}

	private void appendFavorite() {
	}

	IndexEntry[] loadFavorites(String targetDirPath) {
	}
}
