package org.fst.shuffle_my_music.v2

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path

class IndexFileServiceTest extends AbstractTest {

	void testCreatesIndexIfIndexFileDoesNotExist() {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		assert !Files.exists(indexPath)
		new IndexFileService().createIndexIfNecessary(mediaLibraryPath)
		assert Files.exists(indexPath)
	}

	void testIndexContainsAllAudioFiles() {
		new IndexFileService().createIndexIfNecessary(mediaLibraryPath)
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		assert [
			'>>Start',
			mediaLibraryPath.resolve('a0/a1/a2.mp3').toString(),
			mediaLibraryPath.resolve('a0.mp3').toString(),
			mediaLibraryPath.resolve('b0.mp3').toString(),
			'<<End'
		]== Files.readAllLines(indexPath)
	}

	void testRecreatesIndexIfIncomplete() {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		Files.write(indexPath, ['>>Start', mediaLibraryPath.resolve('a0/a1/a2.mp3').toString(), mediaLibraryPath.resolve('a0.mp3').toString()])
		new IndexFileService().createIndexIfNecessary(mediaLibraryPath)
		assert [
			'>>Start',
			mediaLibraryPath.resolve('a0/a1/a2.mp3').toString(),
			mediaLibraryPath.resolve('a0.mp3').toString(),
			mediaLibraryPath.resolve('b0.mp3').toString(),
			'<<End'
		]== Files.readAllLines(indexPath)
	}

	void testDoNothingIfIndexIsComplete() {
		Path indexPath = Files.createFile(mediaLibraryPath.resolve('index.txt'))
		List lines = ['>>Start', 'Some kind of monster.mp3' , '<<End']
		Files.write(indexPath, lines)
		new IndexFileService().createIndexIfNecessary(mediaLibraryPath)
		assert lines == Files.readAllLines(indexPath)
	}

	void testReturnsNumberOfIndexedSongs() {
		assert 3 == new IndexFileService().createIndexIfNecessary(mediaLibraryPath)
	}

	void testRetrievesNothingIfIndicesAreNull() {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		Files.write(indexPath, [
			'>>Start',
			mediaLibraryPath.resolve('a0/a1/a2.mp3').toString(),
			mediaLibraryPath.resolve('a0.mp3').toString(),
			mediaLibraryPath.resolve('b0.mp3').toString(),
			'<<End'
		])
		assert []== new IndexFileService().retrieveIndexEntries(mediaLibraryPath, null)
	}

	void testRetrievesNothingIfIndicesAreEmpty() {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		Files.write(indexPath, [
			'>>Start',
			mediaLibraryPath.resolve('a0/a1/a2.mp3').toString(),
			mediaLibraryPath.resolve('a0.mp3').toString(),
			mediaLibraryPath.resolve('b0.mp3').toString(),
			'<<End'
		])
		assert []== new IndexFileService().retrieveIndexEntries(mediaLibraryPath, [] as int[])
	}

	void testRetrievesIndexEntries() {

		new IndexFileService().createIndexIfNecessary(mediaLibraryPath)
		Path indexPath = mediaLibraryPath.resolve('index.txt')

		assert [mediaLibraryPath.resolve('a0/a1/a2.mp3'), mediaLibraryPath.resolve('b0.mp3')]==
		new IndexFileService().retrieveIndexEntries(mediaLibraryPath, [0, 2] as int[])
	}

	void testSkipIndexEntriesOutOfRange() {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		Files.write(indexPath, [
			'>>Start',
			mediaLibraryPath.resolve('a0/a1/a2.mp3').toString(),
			mediaLibraryPath.resolve('a0.mp3').toString(),
			'<<End'
		])

		assert [mediaLibraryPath.resolve('a0/a1/a2.mp3')]==
		new IndexFileService().retrieveIndexEntries(mediaLibraryPath, [0, 2] as int[])
	}
}
