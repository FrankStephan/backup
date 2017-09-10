package com.frozen_foo.shuffle_my_music_2

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FavoritesServiceTest extends GroovyTestCase {

	Path testPath = Paths.get(this.getClass().getSimpleName())

	Path favoritesFilePath = testPath.resolve('favorites.xml')
	IndexEntry[] newFavorites

	IndexEntry[] favorites

	void testCreatesNewFileIfNotExisting() {
		assert !Files.exists(favoritesFilePath)
		invokeAdd()
		assert Files.exists(favoritesFilePath)
	}

	void testAppendsNewEntries() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', 'dir2/song2.mp3')

		newFavorites = [favorite1]
		invokeAdd()

		newFavorites = [favorite2]
		invokeLoad()

		assert [favorite1, favorite2]== favorites
	}

	void testReturnsAppendedEntries() {
		fail()
	}

	void testSkipsDuplicates() {
		fail()
	}

	void containsRelativePaths() {
		fail()
	}

	void containsUpdateTimestamp() {
		fail()
	}

	private void invokeAdd() {
		new FavoritesService().addFavorites(testPath.toString(), newFavorites)
	}

	private void invokeLoad() {
		favorites = new FavoritesService().loadFavorites(testPath)
	}

	@Override
	protected void tearDown() throws Exception {
		testPath.toFile().deleteDir()
		super.tearDown()
	}
}
