package org.fst.shuffle_my_music

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import java.nio.file.Path
import java.nio.file.Paths

class ShuffleServiceTest extends GroovyTestCase {

	Path testDir = Paths.get(this.getClass().getSimpleName())
	Path mediaLibraryDir = testDir.resolve('mediaLibrary')

	int approximateTotalSongCount
	int numberOfSelectedSongs
	List randoms
	MockFor randomService

	List<Path> songs

	void setUp() {
		super.setUp()
		mediaLibraryDir.toFile().mkdirs()
		FileTreeBuilder ftb = new FileTreeBuilder(mediaLibraryDir.toFile())
		ftb {
			a0 {
				a1 { 'a2.mp3'('') } // 0
				b1 { 'b2.jpg'('') } // 1
			}
			'a0.mp3'('') // 2
			'b0.mp3'('') // 3
			'c0.jpg'('') // 4
		}
	}

	void tearDown() {
		super.tearDown()
		testDir.toFile().deleteDir()
	}

	void testSelectSingleSong() {
		prepareTest(5, [2])
		mockRandomService()
		checkServiceReturns([path('a0.mp3')])
	}

	void testSelectsManySongs() {
		prepareTest(5, [0, 2, 3])
		mockRandomService()
		checkServiceReturns([path('a0/a1/a2.mp3'), path('a0.mp3'), path('b0.mp3')])
	}

	void testSkipsNonAudioFiles() {
		prepareTest(5, [0, 1, 2])
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [0, 1, 2]
		mockRandomService()
		checkServiceReturns([path('a0/a1/a2.mp3'), path('a0.mp3')])
	}

	void testDoNothingIfIndexIsOutOfActualRange() {
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [0, 5, 6]
		mockRandomService()
		checkServiceReturns([path('a0/a1/a2.mp3')])
	}

	void testDoNothingIfIndexIsOutOfActualRange2() {
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [5, 0, 6]
		mockRandomService()
		checkServiceReturns([path('a0/a1/a2.mp3')])
	}

	void testDoNothingIfIndexIsOutOfActualRange3() {
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [6, 5, 0]
		mockRandomService()
		checkServiceReturns([path('a0/a1/a2.mp3')])
	}

	void testDuplicatedIndex() {
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [0, 0, 0]
		mockRandomService()
		checkServiceReturns([path('a0/a1/a2.mp3')])
	}

	void testDuplicatedIndex1() {
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [3, 0, 0]
		mockRandomService()
		checkServiceReturns([path('a0/a1/a2.mp3')])
	}

	void testDuplicatedIndex2() {
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [4, 0, 2]
		mockRandomService()
		checkServiceReturns([path('a0/a1/a2.mp3'), path('a0.mp3')])
	}

	void testDuplicatedIndex3() {
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [5, 1, 5]
		mockRandomService()
		checkServiceReturns([])
	}

	void testDuplicatedIndex4() {
		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [1, 1, 1]
		mockRandomService()
		checkServiceReturns([])
	}

	void testSongAreSortedByFileName() {
		mediaLibraryDir.toFile().deleteDir()
		FileTreeBuilder ftb = new FileTreeBuilder(mediaLibraryDir.toFile())
		ftb {
			'a0' { 'a2.mp3'('') } // 0
			'b0' { 'a0.mp3'('') } // 1
			'c0' { 'a1.mp3'('') } // 2
		}

		approximateTotalSongCount = 5
		numberOfSelectedSongs = 3
		randoms = [0, 1, 2]
		mockRandomService()
		List sortedSongs = [path('b0/a0.mp3'), path('c0/a1.mp3'), path('a0/a2.mp3')]
		checkServiceReturns(sortedSongs)
		assert sortedSongs == songs
	}

	private void mockRandomService() {
		randomService = new MockFor(RandomService.class)
		randomService.demand.randoms(1) {int _approximateTotalSongCount, int _numberOfSelectedSongs ->
			assert approximateTotalSongCount == _approximateTotalSongCount
			assert numberOfSelectedSongs == _numberOfSelectedSongs
			return randoms as int[]
		}
	}

	private void checkServiceReturns(List expectedSongs) {
		randomService.use {
			songs = new ShuffleService().selectRandomSongs(mediaLibraryDir, approximateTotalSongCount, numberOfSelectedSongs)
			assert expectedSongs.size() == songs.size()
			assert songs.containsAll(expectedSongs)
		}
	}

	private void prepareTest(approximateTotalSongCount, List randoms) {
		this.randoms = randoms
		this.approximateTotalSongCount = approximateTotalSongCount
		numberOfSelectedSongs = randoms.size()
		mockRandomService()
	}

	private Path path(String path) {
		return mediaLibraryDir.resolve(path)
	}
}
