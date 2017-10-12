package com.frozen_foo.shuffle_my_music_2.impl

import groovy.xml.MarkupBuilder

import com.frozen_foo.shuffle_my_music_2.IndexEntry

class LocalSongsService {

	void createSongsFile(String targetDirPath, List<IndexEntry> indexEntries) {
		File songsFile = new File(targetDirPath, 'songs.xml').getAbsoluteFile()
		new File(targetDirPath).mkdirs()
		songsFile.createNewFile()
		writeSongsToFile(songsFile, indexEntries)
	}

	private String writeSongsToFile(File songsFile, List<IndexEntry> indexEntries) {
		songsFile.withWriter('UTF-8') { songsFileWriter ->
			def markupBuilder = new MarkupBuilder(songsFileWriter)
			markupBuilder.getMkp().xmlDeclaration([version: '1.0', encoding: 'UTF-8'])
			markupBuilder.songs(xmlns: 'http://frozen-foo.com/', timestamp: new Date().getDateTimeString()) {
				for (IndexEntry favorite : indexEntries) {
					song(title: favorite.getFileName(), favorite.getPath())
				}
			}
		}
	}

	List<IndexEntry> loadSongsFile(String targetDirPath) {
		File songsFile = new File(targetDirPath, 'songs.xml').getAbsoluteFile()
		if (songsFile.exists()) {
			return readSongsFromFile(songsFile)
		} else {
			return []
		}
	}

	private List<IndexEntry> readSongsFromFile(File songsFile) {
		String xmlString = songsFile.getText('UTF-8')
		if (!xmlString.isEmpty()) {
			def songsXml = new XmlSlurper().parseText(xmlString)
			def songs = []
			songsXml.song.each { def song ->
				IndexEntry indexEntry = new IndexEntry()
				indexEntry.setFileName(song.@title.text())
				indexEntry.setPath(song.text())
				songs.add(indexEntry)
			}
			return songs
		} else {
			return []
		}
	}
}
