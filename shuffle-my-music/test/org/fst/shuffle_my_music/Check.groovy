package org.fst.shuffle_my_music

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class Check extends GroovyTestCase {

	void test() {
		String targetPath  = 'X:/Media/shuffly-my-music'
		Path targetDir = Paths.get(targetPath)
		println Paths.get(targetPath).toUri().toString()

		Files.createFile(targetDir.resolve('songs.txt'))
		Files.write(targetDir.resolve('songs.txt'), songs*.toUri().toString(), StandardOpenOption.WRITE)
	}
}
