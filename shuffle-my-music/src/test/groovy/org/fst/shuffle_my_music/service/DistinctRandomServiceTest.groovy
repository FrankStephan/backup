package org.fst.shuffle_my_music.service

import static org.junit.Assert.*

import org.fst.shuffle_my_music.service.DistinctRandomService;

import groovy.mock.interceptor.MockFor

class DistinctRandomServiceTest extends GroovyTestCase {

	void testReturnsNRandoms() {
		int bound = 100
		int randomCount = 10
		assert randomCount == new DistinctRandomService().randoms(bound, randomCount).length
	}

	void testInvokesJavaRandom() {
		int bound = 100
		int randomCount = 10
		MockFor random = new MockFor(Random.class)
		int i = 0
		random.demand.nextInt(randomCount) {int _bound ->
			assert bound == _bound
			return i++
		}
		random.use {
			assert 0..9 == new DistinctRandomService().randoms(bound, randomCount)
		}
	}

	void testRetriesIfSameRandomIsReturnedTwice() {
		int bound = 100
		int randomCount = 5
		MockFor random = new MockFor(Random.class)
		Queue indexQueue = [1, 2, 2, 2, 3, 4, 4, 5] as Queue

		random.demand.nextInt(indexQueue.size()) {int _bound ->
			assert bound == _bound
			return indexQueue.poll()
		}
		random.use {
			assert 1..5 == new DistinctRandomService().randoms(bound, randomCount)
		}
	}
}
