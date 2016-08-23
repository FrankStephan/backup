package org.fst.backup.test

abstract class AbstractTest extends GroovyTestCase {

	String tmpPath
	String sourcePath
	String targetPath
	File sourceDir
	File targetDir

	void setUp() {
		super.setUp()
		tmpPath = getClass().getSimpleName() + '-tmp/'
		sourcePath = tmpPath + 'source/'
		targetPath = tmpPath + 'target/'
		new File(tmpPath).mkdir()
		sourceDir = new File(sourcePath)
		sourceDir.mkdir()
		targetDir = new File(targetPath)
		targetDir.mkdir()
	}

	void tearDown() {
		super.tearDown()
		new File(tmpPath).deleteDir()
	}
}
