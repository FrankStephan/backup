package com.frozen_foo.shuffle_my_music_2

import org.apache.commons.io.LineIterator

class IndexStream {

	def firstLineMatcher = /\d+>>Start/

	LineIterator iterator
	int indexSize = -1
	int entryIndex = 0

	public IndexStream(InputStream inputStream) {
		iterator = new LineIterator(new InputStreamReader(inputStream, 'UTF-8')) {
			protected boolean isValidLine(final String line) {
				return indexSize == -1 || entryIndex < indexSize
			}
		}
	}

	String nextEntry() {
		if (-1 == indexSize)  {
			indexSize()
		}
		entryIndex++
		next()
	}
	
	public int indexSize() {
		if (indexSize == -1) {
			String firstLine = next()
			if (firstLine ==~ firstLineMatcher) {
				int startTagIndex = firstLine.indexOf('>>Start')
				String indexSize = firstLine.substring(0, startTagIndex)
				this.indexSize = indexSize as int
			} else {
				this.indexSize = -1
			}
		}
		return indexSize
	}
	
	private String next() {
		iterator.nextLine()
	}

	boolean empty() {
		!iterator.hasNext()
	}

	void close() {
		iterator.close()
	}
}
