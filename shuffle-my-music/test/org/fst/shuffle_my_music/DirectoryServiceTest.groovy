package org.fst.shuffle_my_music

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class DirectoryServiceTest extends GroovyTestCase {

	Path testDir = Paths.get(this.getClass().getSimpleName())
	Path targetDir = testDir.resolve('targetDir')
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
			'c0.jpg'('') // 4
		}
	}

	void tearDown() {
		super.tearDown()
		testDir.toFile().deleteDir()
	}

	void testTargetDirIsCreatedIfNotExisiting() {
		assert !Files.exists(targetDir)
		new DirectoryService().createDirAndIndexList([], targetDir)
		assert Files.exists(targetDir)
		assert Files.isDirectory(targetDir)
	}

	void testAllSongsAreCopied() {
		List songs = [path('a0.mp3'), path('a0/a1/a2.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetDir)
		assert (targetDir.toFile().list() as List).containsAll(['a0.mp3', 'a2.mp3'])
	}

	void testTargetDirIsClearedIfExisting() {
		List songs = [path('a0.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetDir)
		songs = [path('a0/a1/a2.mp3')]
		new DirectoryService().createDirAndIndexList([], targetDir)
		assert !(targetDir.toFile().list() as List).contains('a0.mp3')
	}

	void testSongListIsCreated() {
		List songs = [path('a0.mp3'), path('a0/a1/a2.mp3')]
		new DirectoryService().createDirAndIndexList(songs, targetDir)
		assert (targetDir.toFile().list() as List).contains('songs.txt')
		assert songs*.toString() == Files.readAllLines(targetDir.resolve('songs.txt'))
	}

	private Path path(String path) {
		return mediaLibraryDir.resolve(path)
	}
}
