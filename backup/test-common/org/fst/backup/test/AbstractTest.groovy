package org.fst.backup.test

import org.fst.backup.model.Increment

abstract class AbstractTest extends GroovyTestCase {

	String tmpPath
	String sourcePath
	String targetPath
	File sourceDir
	File targetDir
	Increment increment

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

	protected Increment createIncrement() {
		increment = new Increment()
				.setSecondsSinceTheEpoch((System.currentTimeMillis() / 1000) as long)
				.setTargetPath(targetPath)
	}
}