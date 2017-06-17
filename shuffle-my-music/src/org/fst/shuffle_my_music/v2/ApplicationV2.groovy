package org.fst.shuffle_my_music.v2

import java.nio.file.Paths


class ApplicationV2 {

	public ApplicationV2(String mediaLibraryPath, String targetPath, int numberOfSelectedSongs) {
		println 'Everyday I\'m Shuffling'
		new ShuffleSongsService().createShuffledSongList(Paths.get(mediaLibraryPath), Paths.get(targetPath), numberOfSelectedSongs)
		println 'Done.'
	}
}
