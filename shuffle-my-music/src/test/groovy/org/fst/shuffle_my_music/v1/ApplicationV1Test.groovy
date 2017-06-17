package org.fst.shuffle_my_music.v1

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import java.nio.file.Path
import java.nio.file.Paths

import org.fst.shuffle_my_music.v1.ApplicationV1
import org.fst.shuffle_my_music.v1.ShuffleService
import org.fst.shuffle_my_music.v2.DirectoryService;

class ApplicationV1Test extends GroovyTestCase {

	void testServiceAreInvokedWithCorrectParams() {
		String mediaLibraryPath = 'path/to/mediaLibrary'
		String targetPath = 'path/to/shuffleDir'
		int approximateTotalSongCount = 666
		int numberOfSelectedSongs = 66

		MockFor shuffleService = new MockFor(ShuffleService)
		MockFor directoryService = new MockFor(DirectoryService)


		List<Path> songs = []
		shuffleService.demand.selectRandomSongs(1) { Path _mediaLibraryDir, int _approximateTotalSongCount, int _numberOfSelectedSongs ->
			assert Paths.get(mediaLibraryPath) == _mediaLibraryDir
			assert approximateTotalSongCount == _approximateTotalSongCount
			assert numberOfSelectedSongs == _numberOfSelectedSongs
			return songs
		}

		directoryService.demand.createDirAndIndexList(1) {List<Path> _songs, Path _targetDir ->
			assert songs == _songs
			assert Paths.get(targetPath) == _targetDir
		}

		shuffleService.use {
			directoryService.use {
				new ApplicationV1(mediaLibraryPath, targetPath, approximateTotalSongCount, numberOfSelectedSongs)
			}
		}
	}
}
