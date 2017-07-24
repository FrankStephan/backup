package com.frozen_foo.shuffle_my_music_2

class ShuffleMyMusicService {

	String[] randomIndexEntries(InputStream indexInputStream, int numberOfSongs) {
		if (numberOfSongs < 1) {
			return []
		}

		IndexStream indexStream = new IndexStream(indexInputStream)
		int indexSize = indexStream.indexSize()
		int[] randoms = new DistinctRandomService().randoms(indexSize, numberOfSongs)
		Arrays.sort(randoms)
		Queue q = (randoms as List) as Queue

		int nextIndex = q.poll()

		List<String> indexEntries = []
		while (!indexStream.empty()) {
			if (nextIndex == indexStream.entryIndex) {
				indexEntries.add(indexStream.nextEntry())
				if (q.empty) {
					break
				} else {
					nextIndex = q.poll()
				}
			} else {
				indexStream.nextEntry()
			}
		}
		return indexEntries
	}
}
