package com.frozen_foo.shuffle_my_music_2

class IndexFileService {

	int indexSize(InputStream indexStream) {
		String firstLine = new BufferedReader(new InputStreamReader(indexStream)).readLine()
		def matcher = /\d+>>Start/

		if (firstLine ==~ matcher) {
			int startTagIndex = firstLine.indexOf('>>Start')
			String indexSize = firstLine.substring(0, startTagIndex)
			return indexSize as int
		} else {
			return -1
		}
	}

	String[] indexEntriesFrom(InputStream indexStream, int[] indices) {
		if (indices != null && indices.length > 0) {
			//			Queue q = indices as Queue
			//			List<String> indexEntries = []
			//
			//			int index = q.poll()
			//			BufferedReader indexReader = new BufferedReader(new InputStreamReader(indexStream))
		} else {
			return [] as String[]
		}
	}
}
