package com.frozen_foo.shuffle_my_music_2

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
		if (-1 == getSize())  {
			indexSize()
		}
		setIndex(getIndex() + 1)
		next()
	}
	
	int indexSize() {
		if (getSize() == -1) {
			String firstLine = next()
			if (firstLine ==~ firstLineMatcher) {
				int startTagIndex = firstLine.indexOf('>>Start')
				String size = firstLine.substring(0, startTagIndex)
				setSize(size as int)
			} else {
				setSize(-1)
			}
		}
		return getSize()
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
