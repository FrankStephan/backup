package org.fst.backup.test

import org.fst.backup.model.Increment

abstract class AbstractTest extends GroovyTestCase {

	String tmpPath
	String sourcePath
	String targetPath
	String restorePath
	File sourceDir
	File targetDir
	File restoreDir
	Increment increment

	void setUp() {
		super.setUp()
		tmpPath = getClass().getSimpleName() + '-tmp/'
		sourcePath = tmpPath + 'source/'
		targetPath = tmpPath + 'target/'
		restorePath = tmpPath + 'restore/'

		new File(tmpPath).mkdir()
		sourceDir = new File(sourcePath)
		sourceDir.mkdir()
		targetDir = new File(targetPath)
		targetDir.mkdir()
		restoreDir = new File(restorePath)
		restoreDir.mkdir()
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

	static List<String> subPaths(File parent) {
		List<String> subPaths = []
		parent.eachFileRecurse {File file ->
			String path = file.toPath().toString()
			String subPath = path.replace(parent.toString(), '')
			subPaths.add(subPath)
		}
		return subPaths
	}
}
