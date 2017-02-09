package org.fst.shuffle_my_music

import static org.junit.Assert.*
import groovy.io.FileType

class Check extends GroovyTestCase {

	File root = new File('root')

	void setUp() {
		super.setUp()
		root.mkdir()
		FileTreeBuilder ftb = new FileTreeBuilder(root)
		ftb {
			'a0.suf'('')
			a0 {
				a1 { 'a2.suf'('') }
				b1 { 'b2.suf'('') }
			}
			'b0.suf'('')
			'c0.suf'('')
		}
	}

	void tearDown() {
		super.tearDown()
		root.deleteDir()
	}


	void testContainsAllFiles() {
		def fileList = []
		root.eachFileRecurse(FileType.FILES) {it ->
			fileList.add(it)
		}

		assert fileList.containsAll([new File('root/a0.suf'), new File('root/a0/a1/a2.suf'), new File('root/a0/b1/b2.suf'), new File('root/b0.suf'), new File('root/c0.suf')])
	}

	void testContainsOnlyFiles() {
		def fileList = []
		root.eachFileRecurse(FileType.FILES) {it ->
			fileList.add(it)
		}

		assert 5 == fileList.size()
	}

	void testIndexList() {
		Random random = new Random()
		def indexList = []
		10.times {
			indexList << random.nextInt(26000)
		}
		indexList.sort()

		println indexList
		assert 10 == indexList.size()
	}

	void testBreakRecursion() {
		def fileList = []
		int index = 0
		root.eachFileRecurse(FileType.FILES) {it ->
			if (index == 2) {
				return false
			}
			index++
			fileList.add(it)
		}
		assert 2 == fileList.size()
	}

	void testShuffle() {
		Random random = new Random()
		def indexList = []
		2.times {
			indexList << random.nextInt(5)
		}
		indexList.sort()

		def fileList = []

		int index = 0
		int indexListIndex = 0
		root.eachFileRecurse(FileType.FILES) {it ->
			if (indexListIndex == 2) {
				return false
			}

			if (indexList[indexListIndex] == index) {
				fileList.add(it)
				indexListIndex++
			}
			index++
		}

		assert 2 == fileList.size()
	}

	void testCreateFilePathsFile() {
		Random random = new Random()
		def indexList = []
		2.times {
			indexList << random.nextInt(5)
		}
		indexList.sort()

		def fileList = []

		int index = 0
		int indexListIndex = 0
		root.eachFileRecurse(FileType.FILES) {it ->
			if (indexListIndex == 2) {
				return false
			}

			if (indexList[indexListIndex] == index) {
				fileList.add(it)
				indexListIndex++
			}
			index++
		}

		File filePaths = new File(root, 'files')
		fileList.each {File it ->
			filePaths << it.absolutePath
			filePaths << System.lineSeparator()
		}

		println filePaths.readLines()

		assert filePaths.readLines() == fileList*.absolutePath
	}
}
