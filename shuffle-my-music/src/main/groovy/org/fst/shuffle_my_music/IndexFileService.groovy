package org.fst.shuffle_my_music

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream


class IndexFileService {

	int createIndexIfNecessary(Path mediaLibraryPath) {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		int indexSize = -1
		if (!Files.exists(indexPath)) {
			indexSize = createIndex(indexPath, mediaLibraryPath)
		} else {
			println 'Calculating index size'
			long currentMillis = System.currentTimeMillis()
			String[] indexEntries = Files.readAllLines(indexPath).toArray()
			if ('>>Start' != indexEntries[0] || '<<End' != indexEntries[indexEntries.length-1]) {
				Files.delete(indexPath)
				indexSize = createIndex(indexPath, mediaLibraryPath)
			} else {
				indexSize =  removeStartAndEndTags(indexEntries).length
			}
			println 'Calculated index size ' + indexSize + ' within ' + ((System.currentTimeMillis() - currentMillis) / 1000d) + ' sec'
		}

		return indexSize
	}

	Path[] retrieveIndexEntries(Path mediaLibraryPath, Integer[] indices) {
		println 'Retrieving index entries'
		long currentMillis = System.currentTimeMillis()
		List<Path> selectedPaths = []
		if (indices != null) {
			Path[] indexPaths = indexPaths(mediaLibraryPath)
			indices.each { int index ->
				if (index < indexPaths.length) {
					selectedPaths.add(indexPaths[index])
				}
			}
		}
		println 'Calculated indexEntries within ' + ((System.currentTimeMillis() - currentMillis) / 1000d) + ' sec'
		return selectedPaths as Path[]
	}

	private Path[] indexPaths(Path mediaLibraryPath) {
		Path indexPath = mediaLibraryPath.resolve('index.txt')
		String[] indexEntries = Files.readAllLines(indexPath).toArray()
		String[] trimmedIndexEntries = removeStartAndEndTags(indexEntries)
		return toPaths(trimmedIndexEntries)
	}

	private String[] removeStartAndEndTags(String[] indexEntries) {
		return indexEntries[1..indexEntries.length-2]
	}

	private Path[] toPaths(String[] indexEntries) {
		indexEntries.collect { String pathName -> Paths.get(pathName) }
	}

	private int createIndex(Path indexPath, Path mediaLibraryPath) {
		int indexSize = 0
		Files.createFile(indexPath)
		Stream<Path> stream = Files.walk(mediaLibraryPath)

		File indexFile = indexPath.toFile()
		indexFile << '>>Start'

		try {
			stream.each { Path it ->
				if (checkAudioContent(it)) {
					indexFile << System.lineSeparator()
					indexFile << new String(it.toString().bytes, 'UTF-8')
					indexSize++
				}
			}

			indexFile << System.lineSeparator()
			indexFile << '<<End'
		} finally {
			stream.close()
		}

		return indexSize
	}

	private boolean checkAudioContent(Path path) {
		String contentType = Files.probeContentType(path)
		if (contentType != null) {
			boolean isAudio = contentType.startsWith('audio')
			boolean isNotPlayList = !contentType.equals('audio/x-mpegurl')
			return isAudio && isNotPlayList
		} else {
			return false
		}
	}
}
