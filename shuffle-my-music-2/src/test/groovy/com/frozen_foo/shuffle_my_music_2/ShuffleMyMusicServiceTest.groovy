package com.frozen_foo.shuffle_my_music_2

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

class ShuffleMyMusicServiceTest extends GroovyTestCase {

	void testObtainsDistinctRandoms() {
		int indexSize = 100
		int numberOfSongs = 10
		MockFor disitinctRandomService = new MockFor(DistinctRandomService.class)
		disitinctRandomService.demand.randoms(1) {int bound, int randomCount ->
			assert indexSize == bound
			assert numberOfSongs == randomCount
			return [] as int[]
		}

		disitinctRandomService.use {
			new ShuffleMyMusicService().relativePathsOfRandomSongs(indexSize, null, numberOfSongs)
		}
	}
}
