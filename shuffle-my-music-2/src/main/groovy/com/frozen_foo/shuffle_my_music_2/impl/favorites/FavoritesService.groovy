package com.frozen_foo.shuffle_my_music_2.impl.favorites

import com.frozen_foo.shuffle_my_music_2.IndexEntry


class FavoritesService extends AbstractFavoritesService {

	IndexEntry[] saveFavorites(String targetDirPath, List<IndexEntry> newFavorites, boolean append) {
		File favoritesFile = createOrGetFile(targetDirPath)
		List<IndexEntry> favorites
		if (append) {
			favorites = calculateResultingFavorites(favoritesFile, newFavorites)
		} else {
			favorites = newFavorites
		}
		favoritesFile.withWriter('UTF-8') { favoritesFileWriter ->
			writeFavorites(favoritesFileWriter, favorites)
		}
		return favorites
	}

	private List<IndexEntry> calculateResultingFavorites(File favoritesFile, List<IndexEntry> newFavorites) {
		List<IndexEntry> favorites = readFavoritesFromFile(favoritesFile) as List
		newFavorites.each { IndexEntry it -> favorites << it }
		removeDuplicates(favorites)
	}

	private List<IndexEntry> removeDuplicates(List<IndexEntry> favorites) {
		favorites.unique()
	}

	List<IndexEntry> loadFavorites(String targetDirPath) {
		File favoritesFile = createOrGetFile(targetDirPath)
		return readFavoritesFromFile(favoritesFile)
	}

	private List<IndexEntry> readFavoritesFromFile(File favoritesFile) {
		String xmlString = favoritesFile.getText('UTF-8')
		if (!xmlString.isEmpty()) {
			def favoritesXml = new XmlSlurper().parseText(xmlString)
			return extractFavorites(favoritesXml)
		} else {
			return []
		}
	}
}
