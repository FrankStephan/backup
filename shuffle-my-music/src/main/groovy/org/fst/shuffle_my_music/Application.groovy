package org.fst.shuffle_my_music

import java.nio.file.Paths


class Application {

	public Application(String mediaLibraryPath, String targetPath, int numberOfSelectedSongs) {
		println 'Everyday I\'m Shuffling'
		new ShuffleSongsService().createShuffledSongList(Paths.get(mediaLibraryPath), Paths.get(targetPath), numberOfSelectedSongs)
		println 'Done.'
	}
}
