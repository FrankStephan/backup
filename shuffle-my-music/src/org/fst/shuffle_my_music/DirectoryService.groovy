package org.fst.shuffle_my_music

import java.nio.file.Files
import java.nio.file.Path

class DirectoryService {

	void createDirAndIndexList(List<Path> songs, Path targetDir) {
		if (Files.exists(targetDir)) {
			targetDir.toFile().deleteDir()
		}
		Files.createDirectories(targetDir)
		Files.createFile(targetDir.resolve('songs.txt'))
		Files.write(targetDir.resolve('songs.txt'), songs*.toString())
		songs.each {
			Files.copy(it, targetDir.resolve(it.fileName))
		}
	}
}
