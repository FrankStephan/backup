package com.frozen_foo.shuffle_my_music_2.impl

import java.nio.file.Files
import java.nio.file.Path

class CreateIndexService {

	public void createIndex(Path mediaLibraryPath) {
		def indexPath = mediaLibraryPath.resolve('index.txt')
		if (Files.exists(indexPath)) {
			Files.delete(indexPath)
		}
		Files.createFile(indexPath)
		File indexFile = indexPath.toFile()

		String fileContent = ''
		int indexSize = 0
		mediaLibraryPath.toFile().eachFileRecurse { File it ->
			Path path = it.toPath()
			if (checkAudioContent(it)) {
				Path relativePath = mediaLibraryPath.relativize(path)
				String relativePathString = relativePath.toString().replace('\\', '/')
				fileContent = fileContent.plus(new String(relativePathString.bytes, 'UTF-8')).plus(System.lineSeparator())
				if (indexSize % 1000 == 0) {
					println indexSize
				}
				indexSize++
			}
		}

		String firstLine = String.valueOf(indexSize).plus('>>Start').plus(System.lineSeparator())
		fileContent = firstLine.plus(fileContent)
		fileContent = fileContent.plus('<<End')
		indexFile << fileContent
	}

	private boolean checkAudioContent(File file) {
		String contentType = Files.probeContentType(file.toPath())
		if (contentType != null) {
			boolean isAudio = contentType.startsWith('audio')
			boolean isNotPlayList = !contentType.equals('audio/x-mpegurl')
			return isAudio && isNotPlayList
		} else {
			return false
		}
	}
}
