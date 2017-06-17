package org.fst.shuffle_my_music

import java.nio.file.Files
import java.nio.file.Path

class ShuffleSongsService {

	void createShuffledSongList(Path mediaLibrary, Path targetPath, int numberOfSongs) {
		def indexFileService = new IndexFileService()
		int indexSize = indexFileService.createIndexIfNecessary(mediaLibrary)
		int[] randoms = new DistinctRandomService().randoms(indexSize, numberOfSongs)
		Path[] indexEntries = indexFileService.retrieveIndexEntries(mediaLibrary, randoms as Integer[])
		new DirectoryService().createDirAndIndexList(selectExistingEntries(indexEntries as List<Path>), targetPath)
	}

	private List<Path> selectExistingEntries(List<Path> indexEntries) {
		return indexEntries.findAll { Path it -> Files.exists(it) }
	}
}
