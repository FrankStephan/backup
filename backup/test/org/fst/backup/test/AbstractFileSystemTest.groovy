package org.fst.backup.test



abstract class AbstractFileSystemTest extends GroovyTestCase {

	String tmpPath
	String sourePath
	String targetPath
	File sourceDir
	File targetDir

	void setUp() {
		super.setUp()
		tmpPath = getClass().getSimpleName() + '-tmp/'
		sourePath = tmpPath + 'source/'
		targetPath = tmpPath + 'target/'
		new File(tmpPath).mkdir()
		sourceDir = new File(sourePath)
		sourceDir.mkdir()
		targetDir = new File(targetPath)
		targetDir.mkdir()
	}

	void tearDown() {
		super.tearDown()
		new File(tmpPath).deleteDir()
	}
}
