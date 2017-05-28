package org.fst.shuffle_my_music.v2

import java.nio.file.Path
import java.nio.file.Paths

class ShuffleSongsService {

	void createShuffledSongList(Path mediaLibrary, int numberOfSongs) {
		int indexSize = new IndexFileService().createIndexIfNecessary(mediaLibrary)
		int[] randoms = new DistinctRandomService().randoms(indexSize, numberOfSongs)
		Paths[] indexEntries = new IndexFileService().retrieveIndexEntries(mediaLibrary, randoms)
	}
}
