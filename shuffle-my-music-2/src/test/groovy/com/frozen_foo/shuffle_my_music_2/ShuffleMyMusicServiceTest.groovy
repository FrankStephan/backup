package com.frozen_foo.shuffle_my_music_2

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

class ShuffleMyMusicServiceTest extends GroovyTestCase {

	int indexSize = 100
	int numberOfSongs = 10
	Integer[] randoms = [9, 22, 91, 33, 44, 55, 88, 11, 77, 66]

	InputStream inputStream



	@Override
	protected void setUp() throws Exception {
		super.setUp()
		inputStream = new MockFor(InputStream.class).proxyDelegateInstance()
	}

	void testCreateIndexStreamFromInputStream() {
		MockFor indexStream = new MockFor(IndexStream.class, true)
		def dummy = new IndexStream(new ByteArrayInputStream(''.bytes))

		indexStream.demand.with {
			IndexStream() { InputStream _inputStream ->
				assert inputStream == _inputStream
				return dummy
			}

			indexSize(1) { return indexSize }
			empty(1) { return true }
		}
		indexStream.use {
			new ShuffleMyMusicService().randomIndexEntries(inputStream, numberOfSongs)
		}
	}

	void testObtainsIndexSize() {
		StubFor indexStream = new StubFor(IndexStream.class)
		indexStream.demand.indexSize(1) { return indexSize }
		indexStream.demand.empty(1) { return true }
		indexStream.use {
			new ShuffleMyMusicService().randomIndexEntries(inputStream, numberOfSongs)
		}
	}

	void testObtainsDistinctRandoms() {
		MockFor indexStream = new MockFor(IndexStream.class)
		indexStream.demand.indexSize(1) { return indexSize }
		indexStream.demand.empty(1) { return true }
		MockFor disitinctRandomService = new MockFor(DistinctRandomService.class)
		disitinctRandomService.demand.randoms(1) {int bound, int randomCount ->
			assert indexSize == bound
			assert numberOfSongs == randomCount
			return randoms
		}

		indexStream.use {
			disitinctRandomService.use {
				new ShuffleMyMusicService().randomIndexEntries(inputStream, numberOfSongs)
			}
		}
	}

	void testNothingIfNumberOfSongsIsZero() {
		numberOfSongs = 0
		assert[]== new ShuffleMyMusicService().randomIndexEntries(inputStream, numberOfSongs)
	}

	void testSkipIndexOutOfRange() {
		String indexContent = ['20>>Start', 0..19, '<<End'].flatten().join(System.lineSeparator())
		inputStream = new ByteArrayInputStream(indexContent.bytes)
		indexSize = 20
		randoms = [1, 21, 3, 20]
		numberOfSongs = 4
		MockFor disitinctRandomService = new MockFor(DistinctRandomService.class)
		disitinctRandomService.demand.randoms(1) {int bound, int randomCount ->
			return randoms
		}

		disitinctRandomService.use {
			assert ['1', '3']== new ShuffleMyMusicService().randomIndexEntries(inputStream, numberOfSongs)
		}
	}

	void testSelectsSongsFromIndex() {
		String indexContent = ['100>>Start', 0..99, '<<End'].flatten().join(System.lineSeparator())
		inputStream = new ByteArrayInputStream(indexContent.bytes)
		MockFor disitinctRandomService = new MockFor(DistinctRandomService.class)
		disitinctRandomService.demand.randoms(1) {int bound, int randomCount ->
			return randoms
		}

		disitinctRandomService.use {
			String[] indexEntries = new ShuffleMyMusicService().randomIndexEntries(inputStream, numberOfSongs)
			assert numberOfSongs == indexEntries.length
			Arrays.sort(randoms)
			List<String> r =  randoms.collect {int random -> String.valueOf(random)}
			assert randoms.collect {int random -> String.valueOf(random)} == indexEntries
		}
	}

	void testNumberOfSongsLargerThanIndex() {
		String indexContent = ['3>>Start', 0..2, '<<End'].flatten().join(System.lineSeparator())
		inputStream = new ByteArrayInputStream(indexContent.bytes)
		indexSize = 3
		randoms = [0, 1, 2, 3]
		numberOfSongs = 4
		MockFor disitinctRandomService = new MockFor(DistinctRandomService.class)
		disitinctRandomService.demand.randoms(1) {int bound, int randomCount ->
			return randoms
		}

		disitinctRandomService.use {
			assert ['0', '1', '2']== new ShuffleMyMusicService().randomIndexEntries(inputStream, numberOfSongs)
		}
	}
}
