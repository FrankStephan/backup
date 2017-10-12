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

	protected List<IndexEntry> extractFavorites(groovy.util.slurpersupport.GPathResult favoritesXml) {
		def favorites = []
		favoritesXml.song.each { def song ->
			IndexEntry indexEntry = new IndexEntry()
			indexEntry.setFileName(song.@title.text())
			indexEntry.setPath(song.text())
			favorites.add(indexEntry)
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
