package com.frozen_foo.shuffle_my_music_2.impl

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.frozen_foo.shuffle_my_music_2.IndexEntry

class LocalSongsServiceTest extends GroovyTestCase {

	Path testPath
	Path songsFilePath

	List<IndexEntry> songs

	@Override
	protected void setUp() {
		super.setUp()
		testPath = Paths.get(this.getClass().getSimpleName())
		songsFilePath = testPath.resolve('songs.xml')
	}

	void testCreatesLocalIndexFileIfNotExisiting() {
		songsFilePath = songsFilePath.toAbsolutePath()
		assert !Files.exists(songsFilePath)
		invokeAdd()
		assert Files.exists(songsFilePath)
	}

	void testResolvesAbsolutePathForFile() {
		assert !Files.exists(songsFilePath)
		invokeAdd()
		assert Files.exists(songsFilePath.toAbsolutePath())
	}

	void testReturnEmptyArrayIfFileDoesNotExist() {
		assert []== invokeLoad()
	}

	void testLoadsCreatedIndexentries() {
		IndexEntry indexEntry1 = new IndexEntry(fileName: '1.mp3', path: 'a/1.mp3')
		IndexEntry indexEntry2 = new IndexEntry(fileName: '2.mp3', path: 'a/2.mp3')
		songs = [indexEntry1, indexEntry2]
		invokeAdd()
		assert songs == invokeLoad()
	}

	void testHandlesUTF8() {
		IndexEntry indexEntry1 = new IndexEntry(fileName: 'äöß??_.mp3', path: 'dir1\\äöß??_.mp3')
		songs = [indexEntry1]
		invokeAdd()
		assert [indexEntry1]== invokeLoad()
	}

	void testOverridesExistingFile() {
		IndexEntry indexEntry1 = new IndexEntry(fileName: '1.mp3', path: 'a/1.mp3')
		songs = [indexEntry1]
		invokeAdd()
		IndexEntry indexEntry2 = new IndexEntry(fileName: '2.mp3', path: 'a/2.mp3')
		songs = [indexEntry2]
		invokeAdd()
		assert [indexEntry2]== invokeLoad()
	}

	private void invokeAdd() {
		new LocalSongsService().createSongsFile(testPath.toString(), songs)
	}

	private IndexEntry[] invokeLoad() {
		new LocalSongsService().loadSongsFile(testPath.toString())
	}

	@Override
	protected void tearDown() throws Exception {
		testPath.deleteDir()
		super.tearDown()
	}
}
