package org.fst.shuffle_my_music


class DistinctRandomService {

	int[] randoms(int bound, int randomCount) {
		Random random = new Random()
		def randoms = []
		randomCount.times {
			while (true) {
				int nextRandom = random.nextInt(bound)
				if (!randoms.contains(nextRandom)) {
					randoms << nextRandom
					break
				}
			}
		}
		return randoms as int[]
	}
}
