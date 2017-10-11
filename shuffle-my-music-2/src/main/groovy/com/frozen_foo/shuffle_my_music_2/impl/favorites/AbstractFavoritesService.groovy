package com.frozen_foo.shuffle_my_music_2.impl.favorites

import groovy.xml.MarkupBuilder

import com.frozen_foo.shuffle_my_music_2.IndexEntry

abstract class AbstractFavoritesService {

	protected File createOrGetFile(String targetDirPath) {
		File favoritesFile = new File(targetDirPath, 'favorites.xml').getAbsoluteFile()
		if (!favoritesFile.exists()) {
			new File(targetDirPath).mkdirs()
			favoritesFile.createNewFile()
		}
		return favoritesFile
	}

	protected extractFavorites(groovy.util.slurpersupport.GPathResult favoritesXml) {
		IndexEntry[] favorites = new IndexEntry[favoritesXml.song.size()]
		favoritesXml.song.eachWithIndex { def song, int i ->
			favorites[i] = new IndexEntry()
			favorites[i].setFileName(song.@title.text())
			favorites[i].setPath(song.text())
		}
		return favorites
	}

	protected void writeFavorites(Writer writer, List<IndexEntry> favorites) {
		def markupBuilder = new MarkupBuilder(writer)
		markupBuilder.getMkp().xmlDeclaration([version: '1.0', encoding: 'UTF-8'])
		markupBuilder.favorites(xmlns: 'http://frozen-foo.com/', timestamp: new Date().getDateTimeString()) {
			for (IndexEntry favorite : favorites) {
				song(title: favorite.getFileName(), favorite.getPath())
			}
		}
	}
}
