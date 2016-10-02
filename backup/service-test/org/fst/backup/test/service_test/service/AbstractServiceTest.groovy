package org.fst.backup.test.service_test.service

import org.fst.backup.test.AbstractTest

abstract class AbstractServiceTest extends AbstractTest {

	void setUp() {
		super.setUp()
		ServiceTestStep.init(sourceDir, targetDir, restoreDir)
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
