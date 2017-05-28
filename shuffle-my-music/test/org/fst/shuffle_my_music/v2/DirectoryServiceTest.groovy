package org.fst.shuffle_my_music.v2

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path

class DirectoryServiceTest extends AbstractTest {

	void testTargetDirIsCreatedIfNotExisiting() {
		assert !Files.exists(targetPath)
		new DirectoryService().createDirAndIndexList([], targetPath)
		assert Files.exists(targetPath)
		assert Files.isDirectory(targetPath)
	}

	void testAllSongsAreCopied() {
		List songs = [path('a0.mp3'), path('a0/a1/a2.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetPath)
		assert (targetPath.toFile().list() as List).containsAll(['a0.mp3', 'a2.mp3'])
	}

	void testTargetDirIsClearedIfExisting() {
		List songs = [path('a0.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetPath)
		songs = [path('a0/a1/a2.mp3')]
		new DirectoryService().createDirAndIndexList([], targetPath)
		assert !(targetPath.toFile().list() as List).contains('a0.mp3')
	}

	void testSongListIsCreated() {
		List songs = [path('a0.mp3'), path('a0/a1/a2.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetPath)
		assert (targetPath.toFile().list() as List).contains('songs.txt')
		assert songs*.toString() == Files.readAllLines(targetPath.resolve('songs.txt'))
	}

	private Path path(String path) {
		return mediaLibraryPath.resolve(path)
	}
}
