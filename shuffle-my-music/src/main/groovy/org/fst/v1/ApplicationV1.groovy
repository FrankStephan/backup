package org.fst.shuffle_my_music.v1

import java.nio.file.Path
import java.nio.file.Paths

import org.fst.shuffle_my_music.v2.DirectoryService;

class ApplicationV1 {

	public ApplicationV1(String mediaLibraryPath, String targetPath, int approximateTotalSongCount, int numberOfSelectedSongs) {
		println 'Everyday I\'m Shuffling'
		List<Path> songs = new ShuffleService().selectRandomSongs(Paths.get(mediaLibraryPath), approximateTotalSongCount, numberOfSelectedSongs)
		println songs
		new DirectoryService().createDirAndIndexList(songs, Paths.get(targetPath))
		println 'Done.'
	}
}
