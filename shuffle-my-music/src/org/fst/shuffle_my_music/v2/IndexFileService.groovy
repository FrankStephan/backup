package org.fst.shuffle_my_music.v2

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

class IndexFileService {

	void createIndexIfNecessary(Path mediaLibrary) {
		Path indexPath = mediaLibrary.resolve('index.txt')
		if (!Files.exists(indexPath )) {
			createIndex(indexPath, mediaLibrary)
		} else {
			List<String> indexList = Files.readAllLines(indexPath)
			if ('>>Start' != indexList[0] || '<<End' != indexList.last()) {
				Files.delete(indexPath)
				createIndex(indexPath, mediaLibrary)
			}
		}
	}

	private createIndex(Path indexPath, Path mediaLibrary) {
		Files.createFile(indexPath)
		Stream<Path> stream = Files.walk(mediaLibrary)

		File indexFile = indexPath.toFile()
		indexFile << '>>Start'
		stream.each { Path it ->
			if (Files.probeContentType(it)?.startsWith('audio')) {
				indexFile << System.lineSeparator()
				indexFile << it.toString()
			}
		}
		indexFile << System.lineSeparator()
		indexFile << '<<End'
	}
}
