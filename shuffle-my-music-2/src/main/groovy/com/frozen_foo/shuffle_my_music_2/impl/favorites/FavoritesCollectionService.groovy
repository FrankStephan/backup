package com.frozen_foo.shuffle_my_music_2.impl.favorites

import org.xml.sax.SAXParseException

import com.frozen_foo.shuffle_my_music_2.IndexEntry

class FavoritesCollectionService extends AbstractFavoritesService {

	List<IndexEntry> loadFavorites(InputStream stream) {
		try {
			def favoritesXml = new XmlSlurper().parse(stream)
			return extractFavorites(favoritesXml)
		} catch (SAXParseException e) {
			return[]
		}
	}

	List<IndexEntry> join(List<IndexEntry> newFavorites, List<IndexEntry> favorites) {
		return (favorites + newFavorites).unique()
	}

	void writeFavorites(List<IndexEntry> favorites, OutputStream stream) {
		stream.withPrintWriter { favoritesFileWriter ->
			writeFavorites(favoritesFileWriter, favorites)
		}
	}
}
