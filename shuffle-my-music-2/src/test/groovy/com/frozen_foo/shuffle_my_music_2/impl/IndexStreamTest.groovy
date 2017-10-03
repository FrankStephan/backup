package com.frozen_foo.shuffle_my_music_2.impl


import groovy.mock.interceptor.MockFor


class IndexStreamTest extends GroovyTestCase {

	String indexFileContent =
	'''3>>Start
a/b/c.mp3
x/y.mp3
z.mp3
<<End'''

	void testClosesStream() {
		MockFor inputStream = new MockFor(InputStream.class)
		inputStream.demand.close() {}
		new IndexStream(inputStream.proxyDelegateInstance()).close()
	}

	void testIndexSize() {
		InputStream inputStream = new ByteArrayInputStream(indexFileContent.bytes)
		def indexStream = new IndexStream(inputStream)
		assert 3 == indexStream.indexSize()
		assert 3 == indexStream.size
		assert 3 == indexStream.indexSize()
		indexStream.nextEntry()
		assert 3 == indexStream.indexSize()
		assert 3 == indexStream.size
	}

	void testIteratesOnStream() {
		InputStream inputStream = new ByteArrayInputStream(indexFileContent.bytes)
		def indexStream = new IndexStream(inputStream)
		List<String> indexEntries = []
		while (!indexStream.empty()) {
			indexEntries.add(indexStream.nextEntry())
		}
		assert indexFileContent.readLines()[1..3] == indexEntries
	}

	void testObtainIndexSizeAndIterate() {
		InputStream inputStream = new ByteArrayInputStream(indexFileContent.bytes)
		def indexStream = new IndexStream(inputStream)
		assert 3 == indexStream.indexSize()
		List<String> indexEntries = []
		while (!indexStream.empty()) {
			indexEntries.add(indexStream.nextEntry())
		}
		assert indexFileContent.readLines()[1..3] == indexEntries
	}

	void testEntryIndex() {
		InputStream inputStream = new ByteArrayInputStream(indexFileContent.bytes)
		def indexStream = new IndexStream(inputStream)

		indexStream.indexSize()
		assert 0 == indexStream.index
	}

	void testEntryIndex1() {
		InputStream inputStream = new ByteArrayInputStream(indexFileContent.bytes)
		def indexStream = new IndexStream(inputStream)

		indexStream.nextEntry()
		assert 1 == indexStream.index
		indexStream.indexSize()
		assert 1 == indexStream.index
	}

	void testEntryIndex2() {
		InputStream inputStream = new ByteArrayInputStream(indexFileContent.bytes)
		def indexStream = new IndexStream(inputStream)

		indexStream.indexSize()
		indexStream.nextEntry()
		assert 1 == indexStream.index
		indexStream.nextEntry()
		assert 2 == indexStream.index
	}
}
