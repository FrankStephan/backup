package org.fst.backup.test.service_test.service

import org.fst.backup.test.AbstractTest

abstract class AbstractServiceTest extends AbstractTest {

	void setUp() {
		super.setUp()
		ServiceTestStep.init(sourceDir, targetDir)
	}
}
