package com.frozen_foo.shuffle_my_music_2

class ShuffleMyMusicService {

	IndexEntry[] randomIndexEntries(InputStream indexInputStream, int numberOfSongs) {
		if (numberOfSongs < 1) {
			return []
		}

		IndexStream indexStream = new IndexStream(indexInputStream)
		int indexSize = indexStream.indexSize()
		int[] randoms = new DistinctRandomService().randoms(indexSize, numberOfSongs)
		Arrays.sort(randoms)
		Queue q = (randoms as List) as Queue

		int nextIndex = q.poll()

		List<String> paths = []
		while (!indexStream.empty()) {
			if (nextIndex == indexStream.getIndex()) {
				paths.add(indexStream.nextEntry())
				if (q.isEmpty()) {
					break
				} else {
					nextIndex = q.poll()
				}
			} else {
				indexStream.nextEntry()
			}
		}
		return paths.collect { String it ->  indexEntry(it)}
	}

	private IndexEntry indexEntry(String path) {
		String fileName
		int lastPathSeparator = path.lastIndexOf('/')
		if (lastPathSeparator != -1) {
			fileName = path.substring(lastPathSeparator+1, path.length())
		} else {
			fileName = path
		}

		new IndexEntry(fileName: fileName, path: path)
	}
}
