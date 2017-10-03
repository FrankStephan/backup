package com.frozen_foo.shuffle_my_music_2.impl

import groovy.xml.MarkupBuilder

import com.frozen_foo.shuffle_my_music_2.IndexEntry

class LocalSongsService {

	void createSongsFile(String targetDirPath, IndexEntry[] indexEntries) {
		File songsFile = new File(targetDirPath, 'songs.xml').getAbsoluteFile()
		new File(targetDirPath).mkdirs()
		songsFile.createNewFile()
		writeSongsToFile(songsFile, indexEntries)
	}

	private String writeSongsToFile(File songsFile, IndexEntry[] indexEntries) {
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

	IndexEntry[] loadSongsFile(String targetDirPath) {
		File songsFile = new File(targetDirPath, 'songs.xml').getAbsoluteFile()
		if (songsFile.exists()) {
			return readSongsFromFile(songsFile)
		} else {
			return []
		}
	}

	private IndexEntry[] readSongsFromFile(File songsFile) {
		String xmlString = songsFile.getText('UTF-8')
		if (!xmlString.isEmpty()) {
			def songsXml = new XmlSlurper().parseText(xmlString)

			IndexEntry[] songs = new IndexEntry[songsXml.song.size()]
			songsXml.song.eachWithIndex { def song, int i ->
				songs[i] = new IndexEntry()
				songs[i].setFileName(song.@title.text())
				songs[i].setPath(song.text())
			}
			return songs
		} else {
			return []
		}
	}
}
