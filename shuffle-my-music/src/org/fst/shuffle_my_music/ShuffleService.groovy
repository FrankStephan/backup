package org.fst.shuffle_my_music

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

class ShuffleService {

	List<Path> selectRandomSongs(Path mediaLibraryDir, int approximateTotalSongCount, int numberOfSelectedSongs) {
		int[] randoms = new RandomService().randoms(approximateTotalSongCount, numberOfSelectedSongs)
		Arrays.sort(randoms)
		int highestIndex = randoms[randoms.length-1]
		Queue indexQueue = (randoms as List) as Queue

		Stream<Path> stream = Files.walk(mediaLibraryDir)
		try {
			Iterator pathIterator = stream.iterator()
			List<Path> songs = []
			int nextSongIndex = indexQueue.poll()
			int index=0
			while (index <= highestIndex && pathIterator.hasNext()) {
				Path path = pathIterator.next()
				if (path.toFile().isFile()) {
					if (index == nextSongIndex) {
						if (Files.probeContentType(path).startsWith('audio')) {
							songs << path
						}
						if (!indexQueue.isEmpty()) {
							nextSongIndex = indexQueue.poll()
						} else {
							break
						}
					}
					index++
				}
			}
			return songs.sort {a, b -> a.getFileName().compareTo(b.getFileName())}
		} finally {
			stream.close()
		}
	}
}
