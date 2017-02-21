package org.fst.shuffle_my_music

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Check extends GroovyTestCase {

	void test() {
		String targetDir  = 'X:/Media/shuffly-my-music-test'
		Path targetPath = Paths.get(targetDir)
		Files.createDirectories(targetPath)
		List<Path> songs = [
			Paths.get('X:/Media/Musik/_Archiv/02 Der Andi und sein Kind \'Sonnengesicht\'.mp3')
		]

		File songsFile = new File('X:/Media/shuffly-my-music-test', 'songs.txt')
		songsFile.createNewFile()

		Files.write(songsFile.toPath(), songs.collect {it.toUri().toString()})
	}
}
