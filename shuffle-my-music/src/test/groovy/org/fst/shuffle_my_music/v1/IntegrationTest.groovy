package org.fst.shuffle_my_music.v1

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class IntegrationTest extends GroovyTestCase {

	String testPath = this.getClass().getSimpleName() + '/'
	String targetPath = testPath + 'targetDir/'
	String mediaLibraryPath = testPath + 'mediaLibrary/'

	void setUp() {
		super.setUp()
		Path mediaLibraryDir = Files.createDirectories(Paths.get(mediaLibraryPath))
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
		Paths.get(testPath).toFile().deleteDir()
	}


	void testSongsInTargetDirMatchIndexList() {
		new ApplicationV1(mediaLibraryPath, targetPath, 5, 3)
		Path targetDir = Paths.get(targetPath)
		List<String> fileNames =  targetDir.toFile().list() as List
		assert fileNames.size() >= 2

		Path indexFile = Paths.get(targetPath, 'songs.txt')
		assert Files.exists(indexFile)

		String indexFileContent = indexFile.toFile().text
		assert indexFileContent.readLines().size() == fileNames.size() - 1
		fileNames.each { it ->
			if (it != 'songs.txt') {
				assert indexFileContent.contains(it)
			}
		}
	}
}
