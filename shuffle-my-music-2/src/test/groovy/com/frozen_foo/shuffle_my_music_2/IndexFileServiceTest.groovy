package com.frozen_foo.shuffle_my_music_2


class IndexFileServiceTest extends GroovyTestCase {


	String indexFileContent =
	'''3>>Start
a/b/c.mp3
x/y.mp3
z.mp3
<<End'''

	InputStream inputStream

	private InputStream inputStream(String s) {
		inputStream = new ByteArrayInputStream(s.bytes)
		return inputStream
	}

	void testObtainsIndexFromFirstLine() {
		assert 3 == new IndexFileService().indexSize(inputStream(indexFileContent))
	}

	void testReturnsMinusOneIfFirstLineIsInvalid() {
		List<String> lines = indexFileContent.readLines()
		lines.remove(0)
		String indexWithoutFirstLine = lines.join('')
		assert -1 == new IndexFileService().indexSize(inputStream(indexWithoutFirstLine))

		assert -1 == new IndexFileService().indexSize(inputStream('>>Start'))
		assert -1 == new IndexFileService().indexSize(inputStream('xyz>>Start'))
	}

	void testIndexStreamPositionIsOnSecondLineAfterRetrievingIndexSize() {
		InputStream inputStream = inputStream(indexFileContent)
		new IndexFileService().indexSize(inputStream)
		List<String> remainingLines = ['a/b/c.mp3', 'x/y.mp3', 'z.mp3', '<<End']
		assert remainingLines == inputStream.readLines()
	}

	void testRetrievesIndexEntries() {
		List<String> allEntries = ['a/b/c.mp3', 'x/y.mp3', 'z.mp3']

		assert allEntries[0] as String[] == new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [0] as int[])
		assert allEntries[1] as String[] == new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [1] as int[])
		assert allEntries[2] as String[] == new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [2] as int[])
		assert allEntries[0, 1] as String[] == new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [0, 1] as int[])
		assert allEntries[0, 2] as String[] == new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [0, 2] as int[])
		assert allEntries[1, 2] as String[] == new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [1, 2] as int[])
		assert allEntries[0, 1, 2] as String[] == new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [0, 1, 2] as int[])
	}

	void testRetrievesNothingIfIndicesAreNull() {
		assert []== new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), null)
	}

	void testRetrievesNothingIfIndicesAreEmpty() {
		assert []== new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [] as int[])
	}

	void testSkipIndexEntriesOutOfRange() {
		List<String> allEntries = ['a/b/c.mp3', 'x/y.mp3', 'z.mp3']
		assert allEntries[0, 1] as String[] == new IndexFileService().indexEntriesFrom(inputStream(indexFileContent), [0, 1, 3, 26] as int[])
	}

	@Override
	protected void tearDown() throws Exception {
		inputStream.close()
		super.tearDown()
	}
}
