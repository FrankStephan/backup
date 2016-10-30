package org.fst.backup.test.integration.service

import org.fst.backup.test.AbstractTest

abstract class AbstractIntegrationTest extends AbstractTest {

	void setUp() {
		super.setUp()
		IntegrationTestStep.init(sourceDir, targetDir, restoreDir)
	}
}
