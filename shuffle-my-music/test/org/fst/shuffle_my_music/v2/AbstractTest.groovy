package org.fst.shuffle_my_music.v2

import java.nio.file.Path
import java.nio.file.Paths

abstract class AbstractTest extends GroovyTestCase {

	Path testPath = Paths.get(this.getClass().getSimpleName())
	Path mediaLibraryPath = testPath.resolve('mediaLibrary')
	Path targetPath = testPath.resolve('shuffle-my-music')

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

}
