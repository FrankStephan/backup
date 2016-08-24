package org.fst.backup.test.system

import org.fst.backup.test.AbstractTest

abstract class AbstractSystemTest extends AbstractTest {

	void setUp() {
		super.setUp()
		SystemTestStep.init(sourceDir, targetDir)
	}
}
