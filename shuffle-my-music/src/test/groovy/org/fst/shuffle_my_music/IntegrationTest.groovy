package org.fst.shuffle_my_music

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

import org.fst.shuffle_my_music.Application;

class IntegrationTest extends AbstractTest {

	Stream<Path> filesStream

	@Override
	public void tearDown() {
		filesStream?.close()
		super.tearDown()
	}

	void testSongFileExists() {
		new Application(mediaLibraryPath.toString(), targetPath.toString(), 1)
		Path songsFilePath = targetPath.resolve('songs.txt')
		assert Files.exists(songsFilePath)
	}

	void testSongsExist() {
		new Application(mediaLibraryPath.toString(), targetPath.toString(), 2)
		filesStream = Files.list(targetPath)
		int numberOfMp3Files = filesStream.filter {Path child -> child.fileName.toString().endsWith('.mp3') }.count()
		assert 2 == numberOfMp3Files
	}

	void testNoOtherFilesAreContainedInTargetDir() {
		new Application(mediaLibraryPath.toString(), targetPath.toString(), 3)
		filesStream = Files.list(targetPath)
		assert 4 == filesStream.count()
	}
}
