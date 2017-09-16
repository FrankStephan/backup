package com.frozen_foo.shuffle_my_music_2

import groovy.xml.MarkupBuilder
import groovy.xml.MarkupBuilderHelper

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FavoritesServiceTest extends GroovyTestCase {

	Path testPath
	Path favoritesFilePath

	IndexEntry[] newFavorites

	IndexEntry[] favorites

	@Override
	protected void setUp() {
		testPath = Paths.get(this.getClass().getSimpleName())
		favoritesFilePath = testPath.resolve('favorites.xml')
	}

	void testCreatesNewFileIfNotExisting() {
		favoritesFilePath = favoritesFilePath.toAbsolutePath()
		assert !Files.exists(favoritesFilePath)
		invokeAdd()
		assert Files.exists(favoritesFilePath)
	}

	void testResolvesAbsolutePathForFavoritesFile() {
		assert !Files.exists(favoritesFilePath)
		invokeAdd()
		assert Files.exists(favoritesFilePath.toAbsolutePath())
	}

	void testAppendsNewEntry() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')

		newFavorites = [favorite1]
		invokeAdd()

		invokeLoad()
		assert [favorite1]== favorites
	}

	void testAppendsNewEntries1() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		newFavorites = [favorite1]
		invokeAdd()

		newFavorites = [favorite2]
		invokeAdd()

		invokeLoad()
		assert [favorite1, favorite2]== favorites
	}

	void testAppendsNewEntries2() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		newFavorites = [favorite1, favorite2]
		invokeAdd()

		invokeLoad()
		assert [favorite1, favorite2]== favorites
	}

	void testAppendsNewEntries3() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')
		IndexEntry favorite3 = new IndexEntry(fileName: 'song3.mp3', path: 'dir3/song3.mp3')
		IndexEntry favorite4 = new IndexEntry(fileName: 'song4.mp3', path: 'dir4/song4.mp3')

		newFavorites = [favorite1, favorite2]
		invokeAdd()

		newFavorites = [favorite3, favorite4]
		invokeAdd()

		invokeLoad()
		assert [favorite1, favorite2, favorite3, favorite4]== favorites
	}

	void testReturnsAppendedEntries() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		newFavorites = [favorite1, favorite2]
		assert invokeAdd() == newFavorites
	}

	void testSkipsDuplicates() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')
		IndexEntry favorite3 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite4 = new IndexEntry(fileName: 'song2.mp3', path: 'dir1000/song2.mp3')
		IndexEntry favorite5 = new IndexEntry(fileName: 'song9.mp3', path: 'dir1/song1.mp3')

		newFavorites = [favorite1, favorite2, favorite3]
		invokeAdd()

		newFavorites = [favorite3, favorite4, favorite5]
		invokeAdd()

		invokeLoad()
		assert [favorite1, favorite2, favorite4, favorite5]== favorites
	}

	void testHandlesUTF8() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'äöß??_.mp3', path: 'dir1\\äöß??_.mp3')
		newFavorites = [favorite1]
		invokeAdd()
		invokeLoad()
		assert favorites == newFavorites
	}

	private IndexEntry[] invokeAdd() {
		return new FavoritesService().addFavorites(testPath.toString(), newFavorites)
	}

	private void invokeLoad() {
		favorites = new FavoritesService().loadFavorites(testPath.toString())
	}

	@Override
	protected void tearDown() throws Exception {
		testPath.deleteDir()
		super.tearDown()
	}


}
