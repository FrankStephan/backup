package org.fst.shuffle_my_music

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

class RandomServiceTest extends GroovyTestCase {

	void testReturnsNRandoms() {
		int approximateTotalSongCount = 100
		int numberOfSelectedSongs = 10
		assert numberOfSelectedSongs == new RandomService().randoms(approximateTotalSongCount, numberOfSelectedSongs).length
	}

	void testInvokesJavaRandom() {
		int approximateTotalSongCount = 100
		int numberOfSelectedSongs = 10
		MockFor random = new MockFor(Random.class)
		int i = 0
		random.demand.nextInt(numberOfSelectedSongs) {int _approximateTotalSongCount ->
			assert approximateTotalSongCount == _approximateTotalSongCount
			return i++
		}
		random.use {
			assert 0..9 == new RandomService().randoms(approximateTotalSongCount, numberOfSelectedSongs)
		}
	}

	void testRetriesIfSameRandomIsReturnedTwice() {
		int approximateTotalSongCount = 100
		int numberOfSelectedSongs = 5
		MockFor random = new MockFor(Random.class)
		Queue indexQueue = [1, 2, 2, 2, 3, 4, 4, 5] as Queue

		random.demand.nextInt(indexQueue.size()) {int _approximateTotalSongCount ->
			assert approximateTotalSongCount == _approximateTotalSongCount
			return indexQueue.poll()
		}
		random.use {
			assert 1..5 == new RandomService().randoms(approximateTotalSongCount, numberOfSelectedSongs)
		}
	}
}
