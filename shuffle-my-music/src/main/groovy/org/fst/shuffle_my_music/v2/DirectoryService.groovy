package org.fst.shuffle_my_music.v2

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream


class DirectoryService {

	void createDirAndIndexList(List<Path> songs, Path targetDir) {
		if (Files.exists(targetDir)) {
			targetDir.toFile().deleteDir()
		}
		Files.createDirectories(targetDir)
		songs.each {
			Files.copy(it, targetDir.resolve(it.fileName))
		}

		Path songsFilePath = targetDir.resolve('songs.txt')
		File songsFile = Files.createFile(songsFilePath).toFile()

		Stream<Path> songFiles = Files.list(targetDir).filter{ Path it ->  it != songsFilePath }
		try {
			Iterator<Path> it = songFiles.iterator()
			while(it.hasNext()) {
				songsFile << it.next().toAbsolutePath().toString()
				if (it.hasNext()) {
					songsFile << System.lineSeparator()
				}
			}
		} finally {
			songFiles.close()
		}
	}
}
