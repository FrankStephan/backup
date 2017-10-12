package com.frozen_foo.shuffle_my_music_2.impl

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.frozen_foo.shuffle_my_music_2.IndexEntry
import com.frozen_foo.shuffle_my_music_2.impl.favorites.FavoritesCollectionService
import com.frozen_foo.shuffle_my_music_2.impl.favorites.FavoritesService


class FavoritesCollectionServiceTest extends GroovyTestCase {

	Path testPath
	Path favoritesFilePath

	@Override
	protected void setUp() {
		testPath = Paths.get(this.getClass().getSimpleName())
		favoritesFilePath = testPath.resolve('favorites.xml')
	}

	void testResolvesFavoritesFilePath() {
		assert favoritesFilePath.toAbsolutePath().toString() == new FavoritesCollectionService().resolveFavoritesFilePath(testPath.toString())
	}

	void testLoadsEntriesFromStream() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')

		new FavoritesService().addFavorites(testPath.toString(), [favorite1])

		Path favoritesFilePath = Paths.get(new FavoritesCollectionService().resolveFavoritesFilePath(testPath.toString()))
		favoritesFilePath.toFile().withInputStream { InputStream stream ->
			assert [favorite1]== new FavoritesCollectionService().loadFavorites(stream)
		}
	}

	void testHandlesEmptyInputStream() {
		Path favoritesFilePath = Paths.get(new FavoritesCollectionService().resolveFavoritesFilePath(testPath.toString()))
		favoritesFilePath.toFile().withInputStream { InputStream stream ->
			assert []== new FavoritesCollectionService().loadFavorites(stream)
		}
	}

	void testJoinComparesFileNameAndPath() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')
		IndexEntry favorite3 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')

		assert [favorite2, favorite1]== new FavoritesCollectionService().join([favorite1], [favorite2, favorite3])
	}

	void testJoinAppensEntriesToTheEnd() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')
		IndexEntry favorite3 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite4 = new IndexEntry(fileName: 'song2.mp3', path: 'dir1000/song2.mp3')
		IndexEntry favorite5 = new IndexEntry(fileName: 'song9.mp3', path: 'dir1/song1.mp3')

		assert [favorite3, favorite4, favorite5, favorite2]== new FavoritesCollectionService().join([favorite1, favorite2], [favorite3, favorite4, favorite5])
	}

	void testWritesEntryToOutputStream() {
		IndexEntry favorite1 = new IndexEntry(fileName: 'song1.mp3', path: 'dir1/song1.mp3')
		IndexEntry favorite2 = new IndexEntry(fileName: 'song2.mp3', path: 'dir2/song2.mp3')

		Files.createDirectories(testPath)
		Files.createFile(favoritesFilePath)

		favoritesFilePath.toFile().withOutputStream {
			new FavoritesCollectionService().writeFavorites([favorite1, favorite2], it)
		}

		favoritesFilePath.toFile().withInputStream {
			assert [favorite1, favorite2]== new FavoritesCollectionService().loadFavorites(it)
		}
	}

	@Override
	protected void tearDown() throws Exception {
		testPath.deleteDir()
		super.tearDown()
	}
}
