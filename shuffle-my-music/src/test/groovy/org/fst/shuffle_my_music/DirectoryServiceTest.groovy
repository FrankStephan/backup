package org.fst.shuffle_my_music

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

import org.fst.shuffle_my_music.DirectoryService;

class DirectoryServiceTest extends AbstractTest {

	void testTargetDirIsCreatedIfNotExisiting() {
		assert !Files.exists(targetPath)
		new DirectoryService().createDirAndIndexList([], targetPath)
		assert Files.exists(targetPath)
		assert Files.isDirectory(targetPath)
	}

	void testAllSongsAreCopied() {
		List<Path> songs = [path('a0.mp3'), path('a0/a1/a2.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetPath)
		assert (targetPath.toFile().list() as List).containsAll(['a0.mp3', 'a2.mp3'])
	}

	void testTargetDirIsClearedIfExisting() {
		List<Path> songs = [path('a0.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetPath)
		songs = [path('a0/a1/a2.mp3')]
		new DirectoryService().createDirAndIndexList([], targetPath)
		assert !(targetPath.toFile().list() as List).contains('a0.mp3')
	}

	void testSongListIsCreated() {
		List<Path> songs = [path('a0.mp3'), path('a0/a1/a2.mp3')]*.toAbsolutePath()
		new DirectoryService().createDirAndIndexList(songs, targetPath)
		assert (targetPath.toFile().list() as List).contains('songs.txt')
	}

	void testSongListIsSortedAccordingToSystemsFileOrder() {
		List<Path> songs = [path('b0.mp3'), path('a0/a1/a2.mp3'), path('a0.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetPath)
		Stream<Path> pathsStream = Files.list(targetPath).filter { Path it ->
			it.getFileName().toString() != 'songs.txt'
		}.map { Path it ->
			it.toAbsolutePath()
		}
		List<Path> songsInDir = pathsStream.collect()
		pathsStream.close()
		Path songFile = targetPath.resolve('songs.txt')
		println songsInDir*.toString()
		println Files.readAllLines(songFile)
		assert Files.readAllLines(songFile) == songsInDir*.toString()
	}

	private Path path(String path) {
		return mediaLibraryPath.resolve(path)
	}
}
