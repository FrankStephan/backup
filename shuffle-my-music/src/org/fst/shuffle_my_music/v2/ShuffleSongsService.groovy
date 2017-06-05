package org.fst.shuffle_my_music.v2

import java.nio.file.Files;
import java.nio.file.Path
import java.nio.file.Paths

class ShuffleSongsService {
	
	static String TARGET = 'shuffle-my-music'

	void createShuffledSongList(Path mediaLibrary, int numberOfSongs) {
		def indexFileService = new IndexFileService()
		int indexSize = indexFileService.createIndexIfNecessary(mediaLibrary)
		int[] randoms = new DistinctRandomService().randoms(indexSize, numberOfSongs)
		Path[] indexEntries = indexFileService.retrieveIndexEntries(mediaLibrary, randoms as Integer[])
		new DirectoryService().createDirAndIndexList(selectExistingEntries(indexEntries as List<Path>), mediaLibrary.resolve(TARGET))
	}
	
	private List<Path> selectExistingEntries(List<Path> indexEntries) {
		return indexEntries.findAll { Path it -> Files.exists(it) }
	}
}
