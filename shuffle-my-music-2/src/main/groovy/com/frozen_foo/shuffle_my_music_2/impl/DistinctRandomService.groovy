package com.frozen_foo.shuffle_my_music_2.impl


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
		return randoms
	}
}
