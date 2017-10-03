package com.frozen_foo.shuffle_my_music_2.impl

import org.apache.commons.io.LineIterator

class IndexStream {

	def firstLineMatcher = /\d+>>Start/
	int size = -1
	int index = 0
	LineIterator iterator

	public IndexStream(InputStream inputStream) {
		iterator = new LineIterator(new InputStreamReader(inputStream, 'UTF-8')) {
			protected boolean isValidLine(final String line) {
				return IndexStream.this.getSize() == -1 || IndexStream.this.getIndex() < IndexStream.this.getSize()
			}
		}
	}

	String nextEntry() {
		if (-1 == size)  {
			indexSize()
		}
		index++
		next()
	}
	
	int indexSize() {
		if (size == -1) {
			String firstLine = next()
			if (firstLine ==~ firstLineMatcher) {
				int startTagIndex = firstLine.indexOf('>>Start')
				String size = firstLine.substring(0, startTagIndex)
				this.size = size as int
			} else {
				size = -1
			}
		}
		return size
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
