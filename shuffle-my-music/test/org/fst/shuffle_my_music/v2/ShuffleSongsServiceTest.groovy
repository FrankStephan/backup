package org.fst.shuffle_my_music.v2

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import java.nio.file.Path

class ShuffleSongsServiceTest extends AbstractTest {

	int bound = 3
	int numberOfSongs = 2
	int[] randoms = [0, 2]
	MockFor indexFileService = new MockFor(IndexFileService.class)
	MockFor distinctRandomService = new  MockFor(DistinctRandomService.class)

	void testTriggersIndexCreationIfNotExisting() {
		indexFileService.demand.createIndexIfNecessary(1) { Path mediaLibraryPath ->
			assert this.mediaLibraryPath == mediaLibraryPath
			return bound
		}

		distinctRandomService.demand.randoms(1) { int bound, int randomCount ->
			return randoms
		}

		indexFileService.demand.retrieveIndexEntries(1) {Path mediaLibraryPath, int[] indices ->
			return [] as Path[]
		}

		indexFileService.use {
			distinctRandomService.use {
				new ShuffleSongsService().createShuffledSongList(mediaLibraryPath, 0)
			}
		}
	}

	void testCreatesRandoms() {
		indexFileService.demand.createIndexIfNecessary(1) { Path mediaLibraryPath -> return bound }

		distinctRandomService.demand.randoms(1) { int bound, int randomCount ->
			assert this.bound == bound
			assert this.numberOfSongs == randomCount
			return randoms
		}

		indexFileService.demand.retrieveIndexEntries(1) {Path mediaLibraryPath, int[] indices ->
			return [] as Path[]
		}

		indexFileService.use {
			distinctRandomService.use {
				new ShuffleSongsService().createShuffledSongList(mediaLibraryPath, numberOfSongs)
			}
		}
	}

	void testRetrievesSongsFromIndex() {
		indexFileService.demand.createIndexIfNecessary(1) { Path mediaLibraryPath -> return bound }

		distinctRandomService.demand.randoms(1) { int bound, int randomCount ->
			return randoms
		}

		indexFileService.demand.retrieveIndexEntries(1) {Path mediaLibraryPath, int[] indices ->
			assert this.mediaLibraryPath == mediaLibraryPath
			assert this.randoms == indices
			return [mediaLibraryPath.resolve('a0/a1/a2.mp3'), mediaLibraryPath.resolve('b0.mp3')] as Path[]
		}

		indexFileService.use {
			distinctRandomService.use {
				new ShuffleSongsService().createShuffledSongList(mediaLibraryPath, numberOfSongs)
			}
		}
	}
}
