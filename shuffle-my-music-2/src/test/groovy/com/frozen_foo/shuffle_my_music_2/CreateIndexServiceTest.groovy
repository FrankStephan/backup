package com.frozen_foo.shuffle_my_music_2

import static org.junit.Assert.*

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class CreateIndexServiceTest extends GroovyTestCase {

	Path testPath = Paths.get(this.getClass().getSimpleName())
	Path mediaLibraryPath = testPath.resolve('mediaLibrary')

	void setUp() {
		super.setUp()
		mediaLibraryPath.toFile().mkdirs()
		FileTreeBuilder ftb = new FileTreeBuilder(mediaLibraryPath.toFile())
		ftb {
			a0 {
				a1 { 'a2.mp3'('') } // 0
				b1 { 'b2.jpg'('') } // 1
			}
			'a0.mp3'('') // 2
			'b0.mp3'('') // 3
			'c0.unknown'('') // 4
		}
	}

	void tearDown() {
		super.tearDown()
		testPath.toFile().deleteDir()
	}

	void testCreatesIndexIfIndexFileDoesNotExist() {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		assert !Files.exists(indexPath)
		new CreateIndexService().createIndex(mediaLibraryPath)
		assert Files.exists(indexPath)
	}

	void testIndexContainsAllAudioFiles() {
		new CreateIndexService().createIndex(mediaLibraryPath)
		Path indexPath = mediaLibraryPath.resolve('index.txt')

		def lines = Files.readAllLines(indexPath, Charset.forName('UTF-8'))
		assert ['a0/a1/a2.mp3', 'a0.mp3', 'b0.mp3']== lines[1..3]
	}

	void testIndexStartsWithNumberOfEntriesAndStartTag() {
		new CreateIndexService().createIndex(mediaLibraryPath)
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		def lines = Files.readAllLines(indexPath, Charset.forName('UTF-8'))
		assert lines[0].startsWith('3>>Start')
	}

	void testIndexEndsWithEndTag() {
		new CreateIndexService().createIndex(mediaLibraryPath)
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		def lines = Files.readAllLines(indexPath, Charset.forName('UTF-8'))
		assert lines[4] == '<<End'
	}

	void testPlayListAreSkipped() {
		Files.createFile(mediaLibraryPath.resolve('playlist.m3u'))
		new CreateIndexService().createIndex(mediaLibraryPath)
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		def lines = Files.readAllLines(indexPath, Charset.forName('UTF-8'))
		assert !lines.any { it.contains('playlist.m3u') }
	}

	void testDeletesOldIndex() {
		Files.createFile(mediaLibraryPath.resolve('index.txt'))
		new CreateIndexService().createIndex(mediaLibraryPath)
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		def lines = Files.readAllLines(indexPath, Charset.forName('UTF-8'))
		assert 5 == lines.size()
	}
}
