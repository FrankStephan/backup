package org.fst.shuffle_my_music

import groovy.io.FileType

import org.apache.commons.io.FileUtils

class Application {

	static String SOURCE = 'X:/Media/Musik'
	static String TARGET = 'X:/Media/shuffly-my-music'
	static int MAX = 26000
	static int COUNT = 10

	public Application() {

		println 'Everyday I\'m Shuffling'

		File targetFolder = new File(TARGET)
		if (targetFolder.exists()) {
			targetFolder.deleteDir()
		}
		targetFolder.mkdirs()

		def fileList = shuffle()

		fileList.each {it ->
			FileUtils.copyFileToDirectory(it, targetFolder)
		}

		File filePaths = new File(TARGET, 'files.txt')

		fileList.each {File it ->
			filePaths << it.absolutePath
			filePaths << System.lineSeparator()
		}
	}

	private List<File> shuffle() {
		Random random = new Random()
		def indexList = []
		COUNT.times {
			indexList << random.nextInt(MAX)
		}
		indexList.sort()
		println indexList

		def fileList = []
		File sourceFolder = new File(SOURCE)

		int index = 0
		int indexListIndex = 0
		sourceFolder.eachFileRecurse(FileType.FILES) {it ->
			if (indexListIndex == COUNT) {
				return false
			}

			if (indexList[indexListIndex] == index) {
				fileList.add(it)
				indexListIndex++
			}
			index++
		}

		println fileList

		return fileList
	}
}
