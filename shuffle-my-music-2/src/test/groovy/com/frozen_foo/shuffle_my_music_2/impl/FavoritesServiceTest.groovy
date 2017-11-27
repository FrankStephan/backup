package com.frozen_foo.shuffle_my_music_2.impl

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.frozen_foo.shuffle_my_music_2.IndexEntry
import com.frozen_foo.shuffle_my_music_2.impl.favorites.FavoritesService

class FavoritesServiceTest extends GroovyTestCase {

	Path testPath
	Path favoritesFilePath

	List<IndexEntry> newFavorites

	List<IndexEntry> favorites

	@Override
	protected void setUp() {
		testPath = Paths.get(this.getClass().getSimpleName())
		favoritesFilePath = testPath.resolve('favorites.xml')
	}

	void testCreatesNewFileIfNotExisting() {
		favoritesFilePath = favoritesFilePath.toAbsolutePath()
		assert !Files.exists(favoritesFilePath)
		invokeSave(true)
		assert Files.exists(favoritesFilePath)
	}

	void testResolvesAbsolutePathForFile() {
		assert !Files.exists(favoritesFilePath)
		invokeSave(true)
		assert Files.exists(favoritesFilePath.toAbsolutePath())
	}

	void testAppendsNewEntry() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')

		newFavorites = [favorite1]
		invokeSave(true)

		invokeLoad()
		assert [favorite1]== favorites
	}

	void testAppendsNewEntries1() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		newFavorites = [favorite1]
		invokeSave(true)

		newFavorites = [favorite2]
		invokeSave(true)

		invokeLoad()
		assert [favorite1, favorite2]== favorites
	}

	void testAppendsNewEntries2() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		newFavorites = [favorite1, favorite2]
		invokeSave(true)

		invokeLoad()
		assert [favorite1, favorite2]== favorites
	}

	void testAppendsNewEntries3() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')
		IndexEntry favorite3 = new IndexEntry(fileName: 'song3.mp3', path: 'dir3/song3.mp3')
		IndexEntry favorite4 = new IndexEntry(fileName: 'song4.mp3', path: 'dir4/song4.mp3')

		newFavorites = [favorite1, favorite2]
		invokeSave(true)

		newFavorites = [favorite3, favorite4]
		invokeSave(true)

		invokeLoad()
		assert [favorite1, favorite2, favorite3, favorite4]== favorites
	}

	void testReturnsAppendedEntries() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		newFavorites = [favorite1, favorite2]
		assert invokeSave(true) == newFavorites
	}

	void testSkipsDuplicates() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')
		IndexEntry favorite3 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite4 = new IndexEntry(fileName: 'song2.mp3', path: 'dir1000/song2.mp3')
		IndexEntry favorite5 = new IndexEntry(fileName: 'song9.mp3', path: 'dir1/song1.mp3')

		newFavorites = [favorite1, favorite2, favorite3]
		invokeSave(true)

		newFavorites = [favorite3, favorite4, favorite5]
		invokeSave(true)

		invokeLoad()
		assert [favorite1, favorite2, favorite4, favorite5]== favorites
	}

	void testReturnsOverridenFavorites() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		newFavorites = [favorite1, favorite2]
		assert invokeSave(false) == newFavorites
	}

	void testLoadsOverridenFavorites() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		newFavorites = [favorite1, favorite2]
		invokeSave(false)
		invokeLoad()

		assert newFavorites == favorites
	}

	void testOverridesPreviousFavorites() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')
		newFavorites = [favorite1, favorite2]
		invokeSave(false)

		IndexEntry favorite3 = new IndexEntry(fileName: 'song3.mp3', path: 'dir3/song3.mp3')
		newFavorites = [favorite2, favorite3]
		invokeSave(false)

		invokeLoad()
		assert newFavorites == favorites
	}

	void testHandlesUTF8() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'äöß??_.mp3', path: 'dir1\\äöß??_.mp3')
		newFavorites = [favorite1]
		invokeSave(true)
		invokeLoad()
		assert favorites == newFavorites
	}

	private IndexEntry[] invokeSave(boolean append) {
		return new FavoritesService().saveFavorites(testPath.toString(), newFavorites, append)
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
