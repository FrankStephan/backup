package org.fst.shuffle_my_music

import java.nio.file.Paths

import org.fst.shuffle_my_music.service.ShuffleSongsService;


class Application {

	public Application(String mediaLibraryPath, String targetPath, int numberOfSelectedSongs) {
		println 'Everyday I\'m Shuffling'
		new ShuffleSongsService().createShuffledSongList(Paths.get(mediaLibraryPath), Paths.get(targetPath), numberOfSelectedSongs)
		println 'Done.'
	}
}
