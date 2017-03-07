package org.fst.shuffle_my_music.v2

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.fst.shuffle_my_music.v2.IndexFileService;

class IndexFileServiceTest extends GroovyTestCase {

	Path testDir = Paths.get(this.getClass().getSimpleName())
	Path targetDir = testDir.resolve('shuffle-my-music')
	Path mediaLibraryDir = testDir.resolve('mediaLibrary')

	void setUp() {
		super.setUp()
		mediaLibraryDir.toFile().mkdirs()
		FileTreeBuilder ftb = new FileTreeBuilder(mediaLibraryDir.toFile())
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
		testDir.toFile().deleteDir()
	}

	void testCreatesIndexIfIndexFileDoesNotExist() {
		Path indexPath = mediaLibraryDir.resolve('index.txt')
		assert !Files.exists(indexPath)
		new IndexFileService().createIndexIfNecessary(mediaLibraryDir)
		assert Files.exists(indexPath)
	}

	void testIndexContainsAllAudioFiles() {
		new IndexFileService().createIndexIfNecessary(mediaLibraryDir)
		Path indexPath = mediaLibraryDir.resolve('index.txt')
		assert [
			'>>Start',
			mediaLibraryDir.resolve('a0/a1/a2.mp3').toString(),
			mediaLibraryDir.resolve('a0.mp3').toString(),
			mediaLibraryDir.resolve('b0.mp3').toString(),
			'<<End'
		]== Files.readAllLines(indexPath)
	}

	void testRecreatesIndexIfIncomplete() {
		Path indexPath = mediaLibraryDir.resolve('index.txt')
		Files.write(indexPath, [
			'>>Start',
			mediaLibraryDir.resolve('a0/a1/a2.mp3').toString(),
			mediaLibraryDir.resolve('a0.mp3').toString()
		])
		new IndexFileService().createIndexIfNecessary(mediaLibraryDir)
		assert [
			'>>Start',
			mediaLibraryDir.resolve('a0/a1/a2.mp3').toString(),
			mediaLibraryDir.resolve('a0.mp3').toString(),
			mediaLibraryDir.resolve('b0.mp3').toString(),
			'<<End'
		]== Files.readAllLines(indexPath)
	}

	void testDoNothingIfIndexIsComplete() {
		Path indexPath = Files.createFile(mediaLibraryDir.resolve('index.txt'))
		List lines = ['>>Start', 'Some kind of monster.mp3' , '<<End']
		Files.write(indexPath, lines)
		new IndexFileService().createIndexIfNecessary(mediaLibraryDir)
		assert lines == Files.readAllLines(indexPath)
	}
}
