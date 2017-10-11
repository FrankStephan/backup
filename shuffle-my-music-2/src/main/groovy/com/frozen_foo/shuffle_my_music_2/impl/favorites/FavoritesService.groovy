package com.frozen_foo.shuffle_my_music_2.impl.favorites

import com.frozen_foo.shuffle_my_music_2.IndexEntry


class FavoritesService extends AbstractFavoritesService {

	IndexEntry[] addFavorites(String targetDirPath, IndexEntry[] newFavorites) {
		File favoritesFile = createOrGetFile(targetDirPath)
		List<IndexEntry> favorites = calculateResultingFavorites(favoritesFile, newFavorites)
		favoritesFile.withWriter('UTF-8') { favoritesFileWriter ->
			writeFavorites(favoritesFileWriter, favorites)
		}
		return favorites
	}

	private List<IndexEntry> calculateResultingFavorites(File favoritesFile, IndexEntry[] newFavorites) {
		List<IndexEntry> favorites = readFavoritesFromFile(favoritesFile) as List
		newFavorites.each { IndexEntry it -> favorites << it }
		removeDuplicates(favorites)
	}

	private List<IndexEntry> removeDuplicates(List<IndexEntry> favorites) {
		favorites.unique()
	}

	IndexEntry[] loadFavorites(String targetDirPath) {
		File favoritesFile = createOrGetFile(targetDirPath)
		return readFavoritesFromFile(favoritesFile)
	}

	private IndexEntry[] readFavoritesFromFile(File favoritesFile) {
		String xmlString = favoritesFile.getText('UTF-8')
		if (!xmlString.isEmpty()) {
			def favoritesXml = new XmlSlurper().parseText(xmlString)
			return extractFavorites(favoritesXml)
		} else {
			return []
		}
	}
}
