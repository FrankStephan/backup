package org.fst.shuffle_my_music

import java.nio.file.Path
import java.nio.file.Paths

class Application {

	public Application(String mediaLibraryPath, String targetPath, int approximateTotalSongCount, int numberOfSelectedSongs) {
		println 'Everyday I\'m Shuffling'
		List<Path> songs = new ShuffleService().selectRandomSongs(Paths.get(mediaLibraryPath), approximateTotalSongCount, numberOfSelectedSongs)
		println songs
		new DirectoryService().createDirAndIndexList(songs, Paths.get(targetPath))
		println 'Done.'
	}
}
