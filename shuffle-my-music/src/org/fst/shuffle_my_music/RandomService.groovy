package org.fst.shuffle_my_music


class RandomService {

	int[] randoms(int approximateTotalSongCount, int numberOfSelectedSongs) {
		Random random = new Random()
		def randoms = []
		numberOfSelectedSongs.times {
			while (true) {
				int nextRandom = random.nextInt(approximateTotalSongCount)
				if (!randoms.contains(nextRandom)) {
					randoms << nextRandom
					break
				}
			}
		}
		return randoms as int[]
	}
}
