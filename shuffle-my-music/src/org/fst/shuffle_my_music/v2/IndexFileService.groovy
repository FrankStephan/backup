package org.fst.shuffle_my_music.v2

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

class IndexFileService {

	int createIndexIfNecessary(Path mediaLibraryPath) {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		if (!Files.exists(indexPath )) {
			createIndex(indexPath, mediaLibraryPath)
		} else {
			List<String> indexList = Files.readAllLines(indexPath)
			if ('>>Start' != indexList[0] || '<<End' != indexList.last()) {
				Files.delete(indexPath)
				createIndex(indexPath, mediaLibraryPath)
			}
		}

		return Files.readAllLines(indexPath).size() - 2
	}

	Path[] retrieveIndexEntries(Path mediaLibraryPath, int[] indices) {
		List<Path> indexEntries = []
		if (indices != null) {
			Path indexPath = mediaLibraryPath.resolve('index.txt')
			List<String> lines = Files.readAllLines(indexPath)
			removeStartAndEndTags(lines)
			indices.each {int index ->
				if (index < lines.size) {
					indexEntries.add(Paths.get(lines.get(index)))
				}
			}
		}
		return indexEntries as Path[]
	}

	private removeStartAndEndTags(List lines) {
		lines.remove(0)
		lines.remove(lines.size()-1)
	}

	private createIndex(Path indexPath, Path mediaLibraryPath) {
		Files.createFile(indexPath)
		Stream<Path> stream = Files.walk(mediaLibraryPath)

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
